window.updateClock = function() {
    const now = new Date();

    // Format the time with AM/PM indicator
    let hours = now.getHours();
    const minutes = now.getMinutes().toString().padStart(2, "0");
    const seconds = now.getSeconds().toString().padStart(2, "0");
    const amPm = hours >= 12 ? "PM" : "AM";
    hours = hours % 12 || 12; // Convert hours to 12-hour format

    // Format the date
    const dateOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    const dateString = now.toLocaleDateString(undefined, dateOptions);

    // Combine date and time
    const timeString = `${hours}:${minutes}:${seconds} ${amPm}`;
    const clockElement = document.getElementById("clock");
    if (clockElement) {
        clockElement.innerHTML = `<div>${dateString}</div><div>${timeString}</div>`;
    }
};