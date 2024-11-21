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
            refreshAccessToken();
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

            // Добавляем заголовки столбцов
            headers.forEach(headerText => {
                const header = document.createElement('th');
                header.textContent = headerText;
                headerRow.appendChild(header);
            });

            thead.appendChild(headerRow);
            table.appendChild(thead);

            // Создаем тело таблицы
            const tbody = document.createElement('tbody');
            tbody.id = "userTableBody"
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

                        if (new Date() !== "" && new Date(endTimeInputValue) !== "") {
                            alert('Время не может быть пустым.');
                            return;
                        }

                        if (new Date(startTimeInputValue) >= new Date(endTimeInputValue)) {
                            alert('Время начала должно быть раньше времени окончания.');
                            return;
                        }

                        if (new Date(startTimeInputValue) < new Date()) {
                            alert('Время начала не может быть в прошлом.');
                            return;
                        }

                        const updateData = {
                            id: recordIdCell.textContent, // ID записи
                            newStartTime: startTimeInputValue, // Новое время начала
                            newEndTime: endTimeInputValue // Новое время окончания
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
                    const recordId = recordIdCell.textContent; // Получаем ID записи из скрытой ячейки

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
                            row.remove(); // Удаляем строку из таблицы
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

function closeRecordsModal() {
    document.getElementById('recordsModal').style.display = 'none';
}

async function refreshAccessToken() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
            throw new Error('No refresh token available');
        }
        const apiUrl = '/auth/refreshToken';
        const requestData = {
            refreshToken: refreshToken
        };
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        const data = await response.json();
        if (data.accessToken) {
            localStorage.setItem('accessToken', data.accessToken);
            console.log('Login successful');
        } else {
            console.error('Login failed');
        }
    } catch (error) {
        console.error('Error refreshing token:', error);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var loginLink = document.getElementById('loginLink');
    if (loginLink) {
        loginLink.addEventListener('click', function(event) {
            // Предотвращаем стандартное поведение ссылки
            event.preventDefault();

            // Проверяем наличие accessToken в localStorage
            if (localStorage.getItem('accessToken')) {
                // Если токен существует, перенаправляем на страницу профиля
                window.location.href = '/profile';
            } else {
                // Если токена нет, выполняем стандартное поведение ссылки
                window.location.href = this.getAttribute('href');
            }
        });
    }
});

function createButton(iconClass, onClick, id) {
    const button = document.createElement('button');
    button.onclick = onClick;
    button.id = id;
    button.className = 'icon-button'; // Добавьте класс для стилизации, если нужно

    const icon = document.createElement('i');
    icon.className = iconClass;
    button.appendChild(icon);

    return button;
}