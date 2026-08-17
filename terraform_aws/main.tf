# Configuración del proveedor
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Crear una Red Virtual (VPC) para la App
resource "aws_vpc" "main_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true

  tags = {
    Name = "VPC-GestionTurnos"
  }
}

# Crear una Subred 1 (Zona A)
resource "aws_subnet" "public_subnet_a" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = "10.0.11.0/24" # Cambiamos el 1 por 11
  availability_zone = "us-east-1a"
  tags = { Name = "Subred-A" }
}

# Crear una Subred 2 (Zona B)
resource "aws_subnet" "public_subnet_b" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = "10.0.12.0/24" # Cambiamos el 2 por 12
  availability_zone = "us-east-1b"
  tags = { Name = "Subred-B" }
}

# Crear el Grupo de Subredes para la Base de Datos
resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "main_db_subnet_group"
  subnet_ids = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]
  tags = { Name = "DB-Subnet-Group" }
}

# --- NUEVO: CONFIGURACIÓN DE RED PARA INTERNET ---

# 1. Puerta de enlace a Internet (IGW)
resource "aws_internet_gateway" "main_igw" {
  vpc_id = aws_vpc.main_vpc.id
  tags   = { Name = "IGW-GestionTurnos" }
}

# 2. Tabla de rutas para que las subredes tengan internet
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main_igw.id
  }
  tags = { Name = "Public-RouteTable" }
}

# 3. Asociar subredes a la tabla de rutas
resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public_rt.id
}
resource "aws_route_table_association" "b" {
  subnet_id      = aws_subnet.public_subnet_b.id
  route_table_id = aws_route_table.public_rt.id
}

# --- NUEVO: BALANCEADOR DE CARGA (ALB) ---

# 4. Security Group para el Balanceador (Permite entrada HTTP puerto 80)
resource "aws_security_group" "alb_sg" {
  name   = "alb-security-group"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 5. El Balanceador de Carga Real
resource "aws_lb" "app_lb" {
  name               = "gestion-turnos-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]
}

# 6. Target Group (A donde enviará el tráfico el ALB)
resource "aws_lb_target_group" "app_tg" {
  name        = "app-target-group-v2" # Cambiado el nombre para evitar el error de "ResourceInUse"
  port        = 3000 # Puerto de Node.js
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main_vpc.id
  target_type = "ip"

  health_check {
    path = "/" # Cambiado a la raíz ya que server.js tiene una ruta ahí
  }
}

# 7. Listener (Escucha en puerto 80 y manda al Target Group)
resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

# --- NUEVO: REGISTRO DE IMÁGENES (ECR) ---
resource "aws_ecr_repository" "app_repo" {
  name                 = "gestion-turnos-backend"
  image_tag_mutability = "MUTABLE"
  force_delete         = true
}

# --- NUEVO: ALMACENAMIENTO DE DOCUMENTOS (S3) ---
resource "aws_s3_bucket" "app_docs" {
  bucket_prefix = "gestion-turnos-docs-"
  force_destroy = true
}

resource "aws_s3_bucket_public_access_block" "app_docs_block" {
  bucket = aws_s3_bucket.app_docs.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# --- NUEVO: SERVIDOR DE APLICACIONES (ECS FARGATE) ---

# 8. Cluster de ECS
resource "aws_ecs_cluster" "main_cluster" {
  name = "gestion-turnos-cluster"
}

# 9. Rol de Ejecución para ECS (Permisos para que ECS arranque la tarea)
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# 9b. Rol de Tarea para ECS (Permisos para lo que la APP hace adentro: S3, etc.)
resource "aws_iam_role" "ecs_task_role" {
  name = "ecsTaskRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

resource "aws_iam_policy" "s3_access_policy" {
  name        = "S3AccessPolicy"
  description = "Permite a la app subir y leer archivos de S3"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action   = ["s3:PutObject", "s3:GetObject", "s3:ListBucket"]
        Effect   = "Allow"
        Resource = [
          aws_s3_bucket.app_docs.arn,
          "${aws_s3_bucket.app_docs.arn}/*"
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_s3_attach" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.s3_access_policy.arn
}

# --- NUEVO: GRUPO DE LOGS PARA VER ERRORES ---
resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/gestion-turnos"
  retention_in_days = 7
}

# 10. Security Group para el Contenedor (Permite tráfico desde el ALB y directo)
resource "aws_security_group" "ecs_sg" {
  name   = "ecs-security-group"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port       = 3000 # Puerto de Node.js
    to_port         = 3000
    protocol        = "tcp"
    cidr_blocks     = ["0.0.0.0/0"] # CAMBIO: Permitir acceso desde cualquier red para pruebas
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 11. Definición de la Tarea (Task Definition)
resource "aws_ecs_task_definition" "app_task" {
  family                   = "gestion-turnos-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([{
    name  = "backend-container"
    image = "${aws_ecr_repository.app_repo.repository_url}:latest"
    portMappings = [{
      containerPort = 3000
      hostPort      = 3000
    }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = "/ecs/gestion-turnos"
        "awslogs-region"        = "us-east-1"
        "awslogs-stream-prefix" = "ecs"
      }
    }
    environment = [
      { name = "PORT", value = "3000" },
      { name = "MONGODB_URI", value = var.mongodb_uri },
      { name = "JWT_SECRET", value = var.jwt_secret },
      { name = "S3_BUCKET_NAME", value = aws_s3_bucket.app_docs.id },
      { name = "DB_HOST", value = aws_db_instance.gestion_turnos_db.address },
      { name = "DB_NAME", value = aws_db_instance.gestion_turnos_db.db_name },
      { name = "DB_USER", value = aws_db_instance.gestion_turnos_db.username },
      { name = "DB_PASS", value = var.db_password },
      { name = "EMAIL_USER", value = var.email_user },
      { name = "EMAIL_PASS", value = var.email_password }
    ]
  }])
}

# 12. El Servicio ECS que mantiene todo corriendo
resource "aws_ecs_service" "main_service" {
  name            = "gestion-turnos-service"
  cluster         = aws_ecs_cluster.main_cluster.id
  task_definition = aws_ecs_task_definition.app_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app_tg.arn
    container_name   = "backend-container"
    container_port   = 3000
  }

  # Evitar que Terraform revierta el número de tareas si el Auto Scaling lo cambia
  lifecycle {
    ignore_changes = [desired_count]
  }
}

# --- NUEVO: AUTO-ESCALADO (Para ahorrar costos y soportar tráfico) ---

resource "aws_appautoscaling_target" "ecs_target" {
  max_capacity       = 3 # Máximo 3 contenedores si hay mucho tráfico
  min_capacity       = 1 # Mínimo 1 siempre activo
  resource_id        = "service/${aws_ecs_cluster.main_cluster.name}/${aws_ecs_service.main_service.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}

# Política de escalado basada en uso de CPU (70%)
resource "aws_appautoscaling_policy" "ecs_policy_cpu" {
  name               = "cpu-autoscaling"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.ecs_target.resource_id
  scalable_dimension = aws_appautoscaling_target.ecs_target.scalable_dimension
  service_namespace  = aws_appautoscaling_target.ecs_target.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ECSServiceAverageCPUUtilization"
    }
    target_value = 70.0 # Escalar cuando el CPU pase del 70%
  }
}

# 1. Crear un Grupo de Seguridad (Firewall) para la Base de Datos
resource "aws_security_group" "db_sg" {
  name        = "db-security-group"
  vpc_id      = aws_vpc.main_vpc.id

  # Permitir entrada de datos (Puerto 5432 para PostgreSQL) desde cualquier red
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    cidr_blocks     = ["0.0.0.0/0"] # CAMBIO: Permitir acceso externo para gestión
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 2. Crear la Base de Datos RDS (PostgreSQL)
resource "aws_db_instance" "gestion_turnos_db" {
  allocated_storage      = 20
  engine                 = "postgres"
  engine_version         = "15"
  instance_class         = "db.t3.micro"
  db_name                = "gestion_turnos"
  username               = "admin_turnos"
  password               = var.db_password
  parameter_group_name   = "default.postgres15"
  skip_final_snapshot    = true
  publicly_accessible    = true # CAMBIO: Permitir acceso desde internet
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.db_subnet_group.name # Agregamos esto
}

# --- OUTPUTS: DATOS PARA CONFIGURAR TU APP ---
output "app_url" {
  value       = "http://${aws_lb.app_lb.dns_name}"
  description = "Copia esta URL en el BASE_URL de tu RetrofitClient.kt"
}

output "rds_endpoint" {
  value       = aws_db_instance.gestion_turnos_db.endpoint
  description = "Endpoint de la base de datos PostgreSQL"
}

output "s3_bucket_name" {
  value       = aws_s3_bucket.app_docs.id
  description = "Nombre del bucket para documentos médicos"
}

output "ecr_repository_url" {
  value       = aws_ecr_repository.app_repo.repository_url
  description = "URL del repositorio para subir tu imagen Docker"
}
