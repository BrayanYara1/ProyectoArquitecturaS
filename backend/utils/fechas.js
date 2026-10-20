/**
 * Helper para formatear cadenas de hora "HH:MM" a formato 12 horas con AM/PM.
 */
function formatAMPM(time) {
    if (!time || typeof time !== 'string' || !time.includes(':')) return time;
    let [hours, minutes] = time.split(':');
    hours = parseInt(hours, 10);
    if (isNaN(hours)) return time;
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    return `${hours}:${minutes} ${ampm}`;
}

module.exports = {
    formatAMPM
};
