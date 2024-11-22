document.addEventListener('DOMContentLoaded', async function() {
    var loginButton = document.getElementById('loginButton');

    if (loginButton) {
        loginButton.addEventListener('click', async function() {
            var formData = {
                username: document.getElementById('username').value,
                password: document.getElementById('password').value,
            };

            try {
                const response = await fetch('/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(formData)
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok.');
                }

                const data = await response.json();

                if (data.result === true && data.accessToken) {
                    localStorage.setItem('userId', data.userId)
                    localStorage.setItem('accessToken', data.accessToken);
                    localStorage.setItem('refreshToken', data.refreshToken);
                    console.log('Login successful');
                    window.location.href = '/profile';
                } else {
                    console.error('Login failed');
                }
            } catch (error) {
                console.error('Error during login process:', error);
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', async function() {
    var registerButton = document.getElementById('registerButton');
    registerButton.addEventListener('click', async function(e) {
        e.preventDefault();

        var userData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            number: document.getElementById('phoneNumber').value,
            username: document.getElementById('username').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value,
            role: document.getElementById('role').value
        };

        if (userData.password !== userData.confirmPassword) {
            alert('Пароли не совпадают!');
            return;
        }

        try {
            const response = await fetch('auth/registration', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (data.success) {
                alert('Регистрация выполнена!');
                window.location.href = '/login';
            } else {
                alert('Ошибка регистрации: ' + data.message);
            }
        } catch (error) {
            console.error('Ошибка при отправке данных:', error);
        }
    });
});

document.addEventListener('DOMContentLoaded', async function() {
    const userProfileDiv = document.getElementById('userContainer');
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
        try {
            const response = await fetch('/api/v1/user/get', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }

            const data = await response.json();

            userProfileDiv.innerHTML = `
            <div id="userPhotoBlock">
                <img src="/img/icon_missing_full.png" id="userPhoto">
            </div>
                    <div id="userProfile">
                <p id="userInfoBlock">Имя: ${data.firstName}</p>
                <p id="userInfoBlock">Фамилия: ${data.lastName}</p>
                <p id="userInfoBlock">Номер телефона: ${data.number}</p>
                <button onclick="showRecordsModal()" id="userButton">Показать все записи</button>
                        </div>
            `;
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            userProfileDiv.innerHTML = `<p>Ошибка загрузки данных профиля.</p>`;
        }
    } else {
        userProfileDiv.innerHTML = `<p>AccessToken отсутствует. Пожалуйста, войдите в систему.</p>`;
    }
});

async function showRecordsModal() {
    const recordsModal = document.getElementById('recordsModal');
    const recordsListDiv = document.getElementById('recordsList');
    const accessToken = localStorage.getItem('accessToken');

    recordsListDiv.innerHTML = '';

    try {
        const response = await fetch('/api/v1/record/all', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();

        if (data.message !== 'Успешно!') {
            alert('Не удалось получить данные о записях');
        } else {
            const table = document.createElement('table');
            table.id = 'userTable';
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            const headers = ['Doctor ID', 'Hospital ID', 'Start Time', 'End Time'];

            headers.forEach(headerText => {
                const header = document.createElement('th');
                header.textContent = headerText;
                headerRow.appendChild(header);
            });

            thead.appendChild(headerRow);
            table.appendChild(thead);

            const tbody = document.createElement('tbody');
            tbody.id = "userTableBody"

            for (let schedule of data.schedules) {
                    const doctorName = await fetchDoctorName(schedule.doctorId);
                    const clinicName = await fetchHospitalName(schedule.hospitalId);
                    if (doctorName) {
                        schedule.doctorId = doctorName;
                    } else {
                        console.error(`Failed to fetch doctor name for doctorId: ${schedule.doctorId}`);
                    }
                    if (clinicName) {
                        schedule.hospitalId = clinicName;
                    } else {
                        console.error(`Failed to fetch clinic name for clinicId: ${schedule.doctorId}`);
                    }
            }
            data.schedules.forEach(record => {
                const row = document.createElement('tr');

                const recordIdCell = document.createElement('td');
                recordIdCell.textContent = record.id;
                recordIdCell.hidden = true;
                row.appendChild(recordIdCell);

                const doctorIdCell = document.createElement('td');
                doctorIdCell.textContent = record.doctorId;
                row.appendChild(doctorIdCell);

                const hospitalIdCell = document.createElement('td');
                hospitalIdCell.textContent = record.hospitalId;
                row.appendChild(hospitalIdCell);

                const startTimeCell = document.createElement('td');
                startTimeCell.textContent = record.startTime ? record.startTime : 'N/A';
                row.appendChild(startTimeCell);

                const endTimeCell = document.createElement('td');
                endTimeCell.textContent = record.endTime ? record.endTime : 'N/A';
                row.appendChild(endTimeCell);

                const originalStartTime = record.startTime;
                const originalEndTime = record.endTime;

                const editButton = createButton('fa fa-edit', () => {
                    const startTimeInput = document.createElement('input');
                    startTimeInput.type = 'datetime-local';
                    startTimeInput.value = record.startTime;
                    startTimeCell.innerHTML = '';
                    startTimeCell.appendChild(startTimeInput);

                    const endTimeInput = document.createElement('input');
                    endTimeInput.type = 'datetime-local';
                    endTimeInput.value = record.endTime;
                    endTimeCell.innerHTML = '';
                    endTimeCell.appendChild(endTimeInput);

                    row.replaceChild(createButton('fa fa-save', async () => {

                        const startTimeInputValue = startTimeInput.value;
                        const endTimeInputValue = endTimeInput.value;

                        if (new Date(startTimeInputValue) >= new Date(endTimeInputValue)) {
                            alert('Время начала должно быть раньше времени окончания.');
                            return;
                        }

                        if (new Date(startTimeInputValue) < new Date()) {
                            alert('Время начала не может быть в прошлом.');
                            return;
                        }

                        const updateData = {
                            id: recordIdCell.textContent,
                            newStartTime: startTimeInputValue,
                            newEndTime: endTimeInputValue
                        };

                        try {
                            const response = await fetch('/api/v1/record/update', {
                                method: 'POST',
                                headers: {
                                    'Authorization': 'Bearer ' + accessToken,
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(updateData)
                            });

                            if (!response.ok) {
                                throw new Error('Network response was not ok ' + response.statusText);
                            }

                            const data = await response.json();
                            console.log(data)
                            startTimeCell.textContent = updateData.newStartTime;
                            endTimeCell.textContent = updateData.newEndTime;

                            const cancelButton = row.querySelector('button[id^="cancelButton"]');
                            cancelButton && cancelButton.remove();

                            const saveButton = row.querySelector('button[id^="saveButton"]');
                            row.replaceChild(editButton, saveButton);

                        } catch (error) {
                            console.error('Не удалось обновить данные записи', error);
                        }
                    }, "saveButton"), editButton);

                    const cancelButton = createButton('fa fa-times', () => {
                        startTimeCell.textContent = originalStartTime;
                        endTimeCell.textContent = originalEndTime;

                        row.removeChild(cancelButton);
                        row.replaceChild(editButton, saveButton);
                    }, "cancelButton");

                    row.insertBefore(cancelButton, deleteButton);
                }, "changeButton");

                row.appendChild(editButton);

                const deleteButton = createButton('fas fa-trash-alt', async () => {
                    const recordId = recordIdCell.textContent;

                    try {
                        const response = await fetch(`/api/v1/record/delete?recordId=${encodeURIComponent(recordId)}`, {
                            method: 'POST',
                            headers: {
                                'Authorization': 'Bearer ' + accessToken
                            }
                        });

                        if (!response.ok) {
                            throw new Error('Network response was not ok: ' + response.statusText);
                        }

                        const data = await response.json();
                        if (data.result !== true) {
                            alert('Не удалось удалить запись.');
                        } else {
                            row.remove();
                        }
                    } catch (error) {
                        console.error('Ошибка при удалении записи:', error);
                    }
                }, `deleteButton-${record.id}`);

                row.appendChild(deleteButton);

                tbody.appendChild(row);
            });

            table.appendChild(tbody);
            recordsListDiv.appendChild(table);
            recordsModal.style.display = 'block';
        }
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
        recordsListDiv.innerHTML = `<p>Ошибка загрузки записей.</p>`;
    }
}

async function fetchDoctorName(doctorId) {

    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await fetch(`/api/v1/doctor/getDoctor?id=${encodeURIComponent(doctorId)}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            },
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return `${data.firstName} ${data.lastName} (${data.specialization})`;
    } catch (error) {
        console.error('Error fetching doctor information:', error);
        return null;
    }
}

async function fetchHospitalName(clinicId) {

    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await fetch(`/api/v1/clinic/getClinic?clinicId=${encodeURIComponent(clinicId)}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            },
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return `${data.name} (${data.address})`;
    } catch (error) {
        console.error('Error fetching doctor information:', error);
        return null;
    }
}

function closeRecordsModal() {
    document.getElementById('recordsModal').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    var loginLink = document.getElementById('loginLink');
    if (loginLink) {
        loginLink.addEventListener('click', function(event) {
            event.preventDefault();

            if (localStorage.getItem('accessToken')) {
                window.location.href = '/profile';
            } else {
                window.location.href = this.getAttribute('href');
            }
        });
    }
});

function createButton(iconClass, onClick, id) {
    const button = document.createElement('button');
    button.onclick = onClick;
    button.id = id;
    button.className = 'icon-button';

    const icon = document.createElement('i');
    icon.className = iconClass;
    button.appendChild(icon);

    return button;
}

document.addEventListener('DOMContentLoaded', function() {
    const accessToken = localStorage.getItem('accessToken');

    const fetchOptions = {
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    };

    let clinicsWithDoctors = [];

    function fetchClinicsAndDoctors() {
        fetch('/api/v1/clinic/all', fetchOptions)
            .then(response => response.json())
            .then(data => {
                clinicsWithDoctors = data.clinics;
                const clinicSelect = document.getElementById('clinic');
                clinicsWithDoctors.forEach(clinic => {
                    const option = document.createElement('option');
                    option.value = clinic.id;
                    option.textContent = clinic.name;
                    clinicSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Ошибка при получении списка клиник и докторов:', error));
    }

    function showDoctorsForClinic(clinicId) {
        const clinic = clinicsWithDoctors.find(clinic => clinic.id.toString() === clinicId);
        const doctors = clinic ? clinic.doctors : [];
        const doctorSelect = document.getElementById('doctor');
        doctorSelect.innerHTML = ''; // Очищаем список докторов

        doctors.forEach(doctor => {
            const option = document.createElement('option');
            option.value = doctor.id;
            option.textContent = `${doctor.firstName} ${doctor.lastName} (${doctor.specialization})`;
            doctorSelect.appendChild(option);
        });
    }

    document.getElementById('clinic').addEventListener('change', function(event) {
        showDoctorsForClinic(event.target.value);
    });

    fetchClinicsAndDoctors();
});

async function submitRecord() {

    var userId;
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        try {
            const response = await fetch('/api/v1/user/get', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }

            const data = await response.json();
            userId = data.id;

        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            alert("Ошибка загрузки данных пользователя.");
        }
    } else {
        alert("AccessToken отсутствует. Пожалуйста, войдите в систему.")
    }

    const doctorId = document.getElementById('doctor').value;
    const clinicId = document.getElementById('clinic').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;

    if (!doctorId || !clinicId || !startTime || !endTime) {
        alert('Пожалуйста, заполните все поля.');
        return;
    }

    if (new Date(startTime) >= new Date(endTime)) {
        alert('Время начала должно быть раньше времени окончания.');
        return;
    }

    const recordData = {
        doctorId: doctorId,
        userId: userId,
        hospitalId: clinicId,
        startTime: startTime,
        endTime: endTime
    };

    if (accessToken) {
        const response = await fetch('/api/v1/record/add', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + accessToken,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(recordData)
        });

        const data = await response.json();
        alert(data.message)

    } else {
        alert("AccessToken отсутствует. Пожалуйста, войдите в систему");
    }
}

document.getElementById('submitRecordButton').addEventListener('click', async function(event) {
    event.preventDefault();
});
