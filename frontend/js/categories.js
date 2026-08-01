// ===============================
// API CONFIG
// ===============================

const API_URL = `${API_BASE_URL}/api/categories`;

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

const config = {
    headers: {
        Authorization: "Bearer " + token,
        "Content-Type": "application/json"
    }
};

let editId = null;

// ===============================
// LOAD DATA
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    loadCategories();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchCategory);

});

// ===============================
// LOAD CATEGORIES
// ===============================

function loadCategories() {

    axios.get(API_URL, config)

        .then(response => {

            const categories = response.data.content;

            const table =
                document.getElementById("categoryTableBody");

            table.innerHTML = "";

            categories.forEach(category => {

                table.innerHTML += `

<tr>

<td>${category.id}</td>

<td>${category.name}</td>

<td>

${
category.active
?
'<span class="active-badge">Active</span>'
:
'<span class="inactive-badge">Inactive</span>'
}

</td>

<td>

${category.createdAt ?
new Date(category.createdAt).toLocaleDateString()
:
"-"}

</td>

<td>

<button
class="edit-btn"
onclick="editCategory(${category.id})">

Edit

</button>

<button
class="delete-btn"
onclick="deleteCategory(${category.id})">

Delete

</button>

</td>

</tr>

`;

            });

        })

        .catch(handleError);

}

// ===============================
// SEARCH
// ===============================

function searchCategory() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#categoryTableBody tr")
        .forEach(row => {

            row.style.display =
                row.innerText
                    .toLowerCase()
                    .includes(value)
                    ? ""
                    : "none";

        });

}

// ===============================
// MODAL
// ===============================

function openModal() {

    editId = null;

    document.getElementById("modalTitle").innerText =
        "Add Category";

    document.getElementById("categoryName").value = "";

    document.getElementById("categoryModal").style.display =
        "flex";

}

function closeModal() {

    document.getElementById("categoryModal").style.display =
        "none";

}

// ===============================
// SAVE CATEGORY
// ===============================

function saveCategory() {

    const category = {

        name: document
            .getElementById("categoryName")
            .value
            .trim()

    };

    if (category.name === "") {

        alert("Category name is required.");

        return;

    }

    if (editId !== null) {

        axios.put(

            `${API_URL}/${editId}`,

            category,

            config

        )

        .then(() => {

            alert("Category updated successfully.");

            closeModal();

            loadCategories();

        })

        .catch(handleError);

    }

    else {

        axios.post(

            API_URL,

            category,

            config

        )

        .then(() => {

            alert("Category added successfully.");

            closeModal();

            loadCategories();

        })

        .catch(handleError);

    }

}

// ===============================
// EDIT CATEGORY
// ===============================

function editCategory(id) {

    axios.get(

        `${API_URL}/${id}`,

        config

    )

    .then(response => {

        const category = response.data;

        editId = category.id;

        document.getElementById("modalTitle").innerText =
            "Edit Category";

        document.getElementById("categoryName").value =
            category.name;

        document.getElementById("categoryModal").style.display =
            "flex";

    })

    .catch(handleError);

}

// ===============================
// DELETE CATEGORY
// ===============================

function deleteCategory(id) {

    if (!confirm("Delete this category?")) {

        return;

    }

    axios.delete(

        `${API_URL}/${id}`,

        config

    )

    .then(() => {

        alert("Category deleted successfully.");

        loadCategories();

    })

    .catch(handleError);

}

// ===============================
// LOGOUT
// ===============================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}

// ===============================
// ERROR HANDLER
// ===============================

function handleError(error) {

    console.error(error);

    if (error.response) {

        if (
            error.response.status === 401 ||
            error.response.status === 403
        ) {

            alert("Session expired.");

            logout();

            return;

        }

        if (error.response.data?.message) {

            alert(error.response.data.message);

            return;

        }

    }

    alert("Something went wrong.");

}