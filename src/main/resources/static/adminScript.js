document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
    loadCurrentUser();
});
async function thisUser() {
    try {
        const response = await fetch("http://localhost:8080/api/admin");
        const data = await response.json();

        // Заполняем шапку страницы для текущего пользователя
        const currentUser = data.currentUser;
        $('#headerUsername').text(currentUser.name);

        // Обрабатываем роли текущего пользователя
        let roles = currentUser.roles.map(role => role.roleName ? role.roleName : "Unknown");
        $('#headerRoles').text(roles.join(', '));

        // Формируем таблицу с пользователями
        const users = data.users;
        const userTableBody = $('#userPanelBody');
        userTableBody.empty();  // Очищаем таблицу перед добавлением новых данных

        // Проходим по каждому пользователю и добавляем его в таблицу
        users.forEach(user => {
            let userRoles = user.roles.map(role => role.roleName ? role.roleName : "Unknown");
            let userRow = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.age}</td>
                    <td>${userRoles.join(', ')}</td>
                    <td><button class="btn btn-sm btn-primary" onclick="prepareEdit(${user.id})" data-bs-toggle="modal" data-bs-target="#editUserModal">Edit</button></td>
                    <td><button class="btn btn-sm btn-danger" onclick="confirmDeleteUser(${user.id})">Delete</button></td>
                </tr>`;
            userTableBody.append(userRow);
        });
    } catch (error) {
        console.error("Error fetching data:", error);
    }
}

$(async function() {
    await thisUser();
});

async function addUser() {
    const newUser = {
        name: $('#newUserName').val(),
        email: $('#newUserEmail').val(),
        age: $('#newUserAge').val(),
        roles: [] // Здесь вы можете добавить роли, если они есть
    };

    try {
        const response = await fetch("http://localhost:8080/api/admin/add-user", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newUser)
        });

        if (response.ok) {
            alert("User added successfully");
            thisUser(); // Перезагружаем таблицу после добавления
        } else {
            alert("Error adding user");
        }
    } catch (error) {
        console.error("Error adding user:", error);
    }
}

async function editUser(id) {
    // Получаем данные из формы редактирования (или можете получать их из таблицы)
    const updatedUser = {
        id: id,
        name: $('#editUserName').val(),
        email: $('#editUserEmail').val(),
        age: $('#editUserAge').val(),
        roles: [] // Здесь нужно добавить роли, если редактируете их
    };

    try {
        const response = await fetch("http://localhost:8080/api/admin/edit_user", {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedUser)
        });

        if (response.ok) {
            alert("User updated successfully");
            thisUser(); // Перезагружаем таблицу после редактирования
        } else {
            alert("Error updating user");
        }
    } catch (error) {
        console.error("Error updating user:", error);
    }
}

async function deleteUser(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/delete-user?id=${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert("User deleted successfully");
            thisUser(); // Перезагружаем таблицу после удаления
        } else {
            alert("Error deleting user");
        }
    } catch (error) {
        console.error("Error deleting user:", error);
    }
}

// Подготовка данных для редактирования
async function prepareEdit(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/get-user?id=${userId}`);

        if (!response.ok) {
            const errorMessage = await response.text(); // Получаем текст ошибки
            console.error(`Error: ${response.status} ${response.statusText}`);
            console.error(errorMessage); // Выводим подробности ошибки
            alert("Error fetching user data");
            return;
        }

        const user = await response.json();

        // Заполним форму редактирования данными пользователя
        $('#editUserName').val(user.name);
        $('#editUserEmail').val(user.email);
        $('#editUserAge').val(user.age);

        // Заполнение ролей, если это необходимо
        const rolesSelect = $('#editUserRoles');
        rolesSelect.empty(); // Очистим список ролей перед добавлением новых
        user.roles.forEach(role => {
            rolesSelect.append(new Option(role.roleName, role.id)); // Используем ID роли для передачи
        });

    } catch (error) {
        console.error("Error fetching user data:", error);
    }
}



// Подтверждение удаления пользователя
function confirmDeleteUser(userId) {
    const confirmed = confirm("Вы уверены, что хотите удалить этого пользователя?");
    if (confirmed) {
        deleteUser(userId);
    }
}
