function formatTimestamp(raw) {
    const date = new Date(raw);
    const pad = n => n.toString().padStart(2, '0');
    return `${pad(date.getDate())}/${pad(date.getMonth()+1)}/${date.getFullYear()} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

function statusDot(state) {
    return `<span class="status-dot ${state ? 'status-on' : 'status-off'}"></span>`;
}

function loadLatestPulse() {
    fetch('/api/latest')
        .then(res => res.json())
        .then(data => {
            const tbody = document.querySelector('#pulse-table tbody');
            tbody.innerHTML = `
                <tr><th>Timestamp</th><td>${formatTimestamp(data.timestamp)}</td></tr>
                <tr><th>Air Temp</th><td>${data.airTemp} °C</td></tr>
                <tr><th>Humidity</th><td>${data.airHum} %</td></tr>
                <tr><th>Air Pressure</th><td>${data.airPres} hPa</td></tr>
                <tr><th>CO₂ ppm</th><td>${data.co2ppm}</td></tr>
                <tr><th>Water Temp</th><td>${data.waterTemp} °C</td></tr>
                <tr><th>Water pH</th><td>${data.waterPH}</td></tr>
                <tr><th>Water EC</th><td>${data.waterEC}</td></tr>
                <tr><th>Water Level</th><td>${data.waterLevel}</td></tr>
                <tr><th>Power Use</th><td>${data.powerUse} W</td></tr>
                <tr><th>Grow/Bloom Mode</th><td>${data.growBloom ? '🌸 Bloom' : '🌱 Grow'}</td></tr>
                <tr><th>Lights On</th><td>${statusDot(data.lightsOn)}</td></tr>
                <tr><th>Pump Active</th><td>${statusDot(data.pumpActive)}</td></tr>
                <tr><th>Fan Active</th><td>${statusDot(data.fanActive)}</td></tr>
                <tr><th>Blower Active</th><td>${statusDot(data.blowerActive)}</td></tr>
                <tr><th>Heater Active</th><td>${statusDot(data.heaterActive)}</td></tr>
            `;
        });
}

loadLatestPulse();
setInterval(loadLatestPulse, 5 * 60 * 1000);
