// ===============================
// API Configuration
// ===============================

const MENU_API = `${API_BASE_URL}/api/menu-items`;
const CATEGORY_API = `${API_BASE_URL}/api/categories`;

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
// Initial Load
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    loadCategories();
    loadMenu();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchTable);

});

// ===============================
// Load Categories
// ===============================

function loadCategories() {

    axios.get(CATEGORY_API, config)

        .then(response => {

            const categories = response.data.content;

            const select =
                document.getElementById("itemCategory");

            select.innerHTML =
                '<option value="">Select Category</option>';

            categories.forEach(category => {

                select.innerHTML += `
                    <option value="${category.id}">
                        ${category.name}
                    </option>
                `;

            });

        })

        .catch(handleError);

}

// ===============================
// Load Menu Items
// ===============================

function loadMenu() {

    // axios.get(MENU_API, config)
    axios.get(
    `${MENU_API}?page=0&size=200`,
    config
)

        .then(response => {

            const items = response.data.content;

            const table =
                document.getElementById("menuTableBody");

            table.innerHTML = "";

            items.forEach(item => {

                table.innerHTML += `

<tr>

<td>${item.id}</td>

<td>${item.name}</td>

<td>${item.description ?? "-"}</td>

<td>₹ ${item.price}</td>

<td>${item.categoryName}</td>

<td>

${item.available
? '<span style="color:green;font-weight:600;">Available</span>'
: '<span style="color:red;font-weight:600;">Unavailable</span>'
}

</td>

<td>

<button class="edit-btn"
onclick="editItem(${item.id})">

Edit

</button>

<button class="delete-btn"
onclick="deleteItem(${item.id})">

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
// Search
// ===============================

function searchTable() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#menuTableBody tr")
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
// Modal
// ===============================

function openModal() {

    editId = null;

    clearForm();

    document.getElementById("modalTitle").innerText =
        "Add Menu Item";

    document.getElementById("menuModal").style.display =
        "flex";

}

function closeModal() {

    document.getElementById("menuModal").style.display =
        "none";

}

// ===============================
// Clear Form
// ===============================

function clearForm() {

    document.getElementById("itemName").value = "";

    document.getElementById("itemDescription").value = "";

    document.getElementById("itemPrice").value = "";

    document.getElementById("itemCategory").value = "";

    document.getElementById("itemStatus").value = "true";

}


// ===============================
// Save Menu Item (Create / Update)
// ===============================

function saveItem() {

    const item = {

        name: document.getElementById("itemName").value.trim(),

        description: document.getElementById("itemDescription").value.trim(),

        price: Number(document.getElementById("itemPrice").value),

        categoryId: Number(document.getElementById("itemCategory").value),

        available: document.getElementById("itemStatus").value === "true"

    };


    // Validation

    if (
        item.name === "" ||
        item.price <= 0 ||
        !item.categoryId
    ) {

        alert("Please fill all required fields.");

        return;

    }



    // UPDATE

    if (editId !== null) {

        axios.put(

            `${MENU_API}/${editId}`,

            item,

            config

        )

        .then(() => {

            alert("Menu item updated successfully.");

            closeModal();

            loadMenu();

        })

        .catch(handleError);

    }

    // CREATE

    else {

        axios.post(

            MENU_API,

            item,

            config

        )

        .then(() => {

            alert("Menu item added successfully.");

            closeModal();

            loadMenu();

        })

        .catch(handleError);

    }

}



// ===============================
// Edit Item
// ===============================

function editItem(id) {

    axios.get(

        `${MENU_API}/${id}`,

        config

    )

    .then(response => {

        const item = response.data;

        editId = item.id;

        document.getElementById("modalTitle").innerText =
            "Edit Menu Item";

        document.getElementById("itemName").value =
            item.name;

        document.getElementById("itemDescription").value =
            item.description ?? "";

        document.getElementById("itemPrice").value =
            item.price;

        document.getElementById("itemCategory").value =
            item.categoryId;

        document.getElementById("itemStatus").value =
            item.available ? "true" : "false";

        document.getElementById("menuModal").style.display =
            "flex";

    })

    .catch(handleError);

}



// ===============================
// Delete Item
// ===============================

function deleteItem(id) {

    if (!confirm("Are you sure you want to delete this menu item?")) {

        return;

    }

    axios.delete(

        `${MENU_API}/${id}`,

        config

    )

    .then(() => {

        alert("Menu item deleted successfully.");

        loadMenu();

    })

    .catch(handleError);

}



// ===============================
// Logout
// ===============================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}



// ===============================
// Error Handler
// ===============================

function handleError(error) {

    console.error(error);

    if (error.response) {

        if (
            error.response.status === 401 ||
            error.response.status === 403
        ) {

            alert("Session expired. Please login again.");

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