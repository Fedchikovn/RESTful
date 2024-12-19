document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
    loadCurrentUser();
    addUser();
    populateRolesDropdown('newUserRoles'); // Заполнение ролей для добавления
    populateRolesDropdown('editUserRoles'); // Заполнение ролей для редактирования
});

// Загрузка всех пользователей
async function loadUsers() {
    try {
        const response = await fetch('/api/admin');
        const users = await response.json();

        const userTableBody = document.getElementById('userPanelBody');
        userTableBody.innerHTML = ''; // Очищаем таблицу

        users.forEach(user => {
            const userRoles = user.roles.join(', ');
            const userRow = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.age}</td>
                    <td>${userRoles}</td>
                    <td><button class="btn btn-sm btn-primary" onclick="prepareEdit(${user.id})" data-bs-toggle="modal" data-bs-target="#editUserModal">Edit</button></td>
                    <td><button class="btn btn-sm btn-danger" onclick="confirmDeleteUser(${user.id})">Delete</button></td>
                </tr>`;
            userTableBody.insertAdjacentHTML('beforeend', userRow);
        });
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

// Загрузка всех ролей
async function fetchRoles() {
    try {
        const response = await fetch("http://localhost:8080/api/admin/roles");
        if (response.ok) {
            return await response.json(); // Получаем массив ролей
        } else {
            console.error("Failed to fetch roles");
            return [];
        }
    } catch (error) {
        console.error("Error fetching roles:", error);
        return [];
    }
}

// Заполнение выпадающего списка ролей
async function populateRolesDropdown(elementId) {
    const roles = await fetchRoles();
    const dropdown = document.getElementById(elementId);

    if (dropdown) {
        dropdown.innerHTML = ''; // Очищаем старые значения
        roles.forEach(role => {
            const option = document.createElement('option');
            option.value = role; // Подставляем название роли
            option.textContent = role; // Отображаем название роли
            dropdown.appendChild(option);
        });
    } else {
        console.error(`Element with id ${elementId} not found`);
    }
}

// Подготовка данных для редактирования
let editUserId = null; // Переменная для хранения ID редактируемого пользователя

async function prepareEdit(userId) {
    editUserId = userId; // Устанавливаем ID редактируемого пользователя

    try {
        const response = await fetch(`/api/admin/${userId}`);
        const user = await response.json();

        if (user) {
            document.getElementById('editUserName').value = user.name || '';
            document.getElementById('editUserEmail').value = user.email || '';
            document.getElementById('editUserAge').value = user.age || '';
            document.getElementById('editUserPassword').value = ''; // Оставляем пароль пустым
            document.getElementById('editUserRoles').innerHTML = ''; // Очистим текущие роли

            // Заполняем выпадающий список ролями
            await populateRolesDropdown('editUserRoles');

            // Выделяем текущие роли пользователя в списке
            const roleOptions = document.getElementById('editUserRoles').options;
            for (let i = 0; i < roleOptions.length; i++) {
                if (user.roles && user.roles.includes(roleOptions[i].value)) {
                    roleOptions[i].selected = true;
                }
            }
        }
    } catch (error) {
        console.error('Error preparing user edit:', error);
    }
}

// Редактирование пользователя
async function editUser() {
    if (!editUserId) {  // Проверяем, есть ли id редактируемого пользователя
        alert("User ID is not set for editing");
        return;
    }

    const selectedRoles = Array.from(document.getElementById('editUserRoles').selectedOptions)
        .map(option => option.value);

    const updatedUser = {
        id: editUserId,  // Используем editUserId
        name: $('#editUserName').val(),
        email: $('#editUserEmail').val(),
        age: $('#editUserAge').val(),
        password: $('#editUserPassword').val() || null, // Если поле пустое, передаем null
        roles: selectedRoles
    };

    try {
        const response = await fetch(`http://localhost:8080/api/admin/${editUserId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedUser)
        });

        if (response.ok) {
            alert("User updated successfully");
            loadUsers(); // Перезагружаем таблицу

            // Закрытие модального окна после успешного обновления
            const editUserModal = bootstrap.Modal.getInstance(document.getElementById('editUserModal')); // Получаем существующий экземпляр
            console.log('Trying to hide modal');
            editUserModal.hide();  // Попытка скрыть окно
            console.log('Modal should be hidden now');
        } else {
            alert("Error updating user");
        }
    } catch (error) {
        console.error("Error updating user:", error);
    }
}

// Загрузка текущего пользователя
async function loadCurrentUser() {
    try {
        const response = await fetch('/api/admin/current');
        const user = await response.json();

        document.getElementById('headerUsername').textContent = user.name;
        document.getElementById('headerRoles').textContent = user.roles.join(', ');
    } catch (error) {
        console.error('Error loading current user:', error);
    }
}

async function addUser() {
    const user = {
        name: document.getElementById('addUserName').value,
        email: document.getElementById('addUserEmail').value,
        age: document.getElementById('addUserAge').value,
        roles: Array.from(document.getElementById('addUserRoles').selectedOptions).map(option => option.value),
        password: document.getElementById('addUserPassword').value
    };

    try {
        const response = await fetch('/api/admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {
            alert("User added successfully");
            loadUsers(); // Обновляем таблицу

            // Закрываем модальное окно
            const addUserModal = bootstrap.Modal.getInstance(document.getElementById('addUserModal'));
            if (addUserModal) {
                addUserModal.hide();
            }
        } else {
            alert("Error adding user");
        }
    } catch (error) {
        console.error('Error adding user:', error);
    }
}

// Удаление пользователя
async function deleteUser(userId) {
    try {
        const response = await fetch(`/api/admin/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('User deleted successfully');
            loadUsers();
        } else {
            alert('Error deleting user');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
    }
}

// Подтверждение удаления
function confirmDeleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        deleteUser(userId);
    }
}