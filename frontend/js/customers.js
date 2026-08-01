// =====================================================
// Restaurant Management System
// Customer Module
// Part 1
// =====================================================

// ===============================
// API
// ===============================

const CUSTOMER_API = "http://localhost:8080/api/customers";

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

    loadCustomers();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchCustomer);

});

// ===============================
// Load Customers
// ===============================

function loadCustomers() {

    axios.get(

        `${CUSTOMER_API}?page=0&size=200`,

        config

    )

    .then(response => {

        const customers = response.data.content;

        const table =
            document.getElementById("customerTableBody");

        table.innerHTML = "";

        customers.forEach(customer => {

            table.innerHTML += `

<tr>

<td>${customer.id}</td>

<td>${customer.name}</td>

<td>${customer.phone}</td>

<td>${customer.email ?? "-"}</td>

<td>${customer.address ?? "-"}</td>

<td>${customer.createdAt ?
customer.createdAt.substring(0,10)
: "-"}</td>

<td>

<button
class="edit-btn"
onclick="editCustomer(${customer.id})">

Edit

</button>

<button
class="delete-btn"
onclick="deleteCustomer(${customer.id})">

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

function searchCustomer() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#customerTableBody tr")
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
        "Add Customer";

    document.getElementById("customerModal").style.display =
        "flex";

}

function closeModal() {

    document.getElementById("customerModal").style.display =
        "none";

}

// ===============================
// Clear Form
// ===============================

function clearForm() {

    document.getElementById("customerName").value = "";

    document.getElementById("customerPhone").value = "";

    document.getElementById("customerEmail").value = "";

    document.getElementById("customerAddress").value = "";

}


// ===============================
// Save Customer
// ===============================

function saveCustomer() {

    const customer = {

        name: document
            .getElementById("customerName")
            .value
            .trim(),

        phone: document
            .getElementById("customerPhone")
            .value
            .trim(),

        email: document
            .getElementById("customerEmail")
            .value
            .trim(),

        address: document
            .getElementById("customerAddress")
            .value
            .trim()

    };


    // Validation

    if (

        customer.name === "" ||

        customer.phone === ""

    ) {

        alert("Please enter customer name and phone.");

        return;

    }


    // UPDATE

    if (editId !== null) {

        axios.put(

            `${CUSTOMER_API}/${editId}`,

            customer,

            config

        )

        .then(() => {

            alert("Customer updated successfully.");

            closeModal();

            loadCustomers();

        })

        .catch(handleError);

    }

    // CREATE

    else {

        axios.post(

            CUSTOMER_API,

            customer,

            config

        )

        .then(() => {

            alert("Customer added successfully.");

            closeModal();

            loadCustomers();

        })

        .catch(handleError);

    }

}



// ===============================
// Edit Customer
// ===============================

function editCustomer(id) {

    axios.get(

        `${CUSTOMER_API}/${id}`,

        config

    )

    .then(response => {

        const customer = response.data;

        editId = customer.id;

        document.getElementById("modalTitle").innerText =
            "Edit Customer";

        document.getElementById("customerName").value =
            customer.name;

        document.getElementById("customerPhone").value =
            customer.phone;

        document.getElementById("customerEmail").value =
            customer.email ?? "";

        document.getElementById("customerAddress").value =
            customer.address ?? "";

        document.getElementById("customerModal").style.display =
            "flex";

    })

    .catch(handleError);

}



// ===============================
// Delete Customer
// ===============================

function deleteCustomer(id) {

    if (

        !confirm(

            "Are you sure you want to delete this customer?"

        )

    ) {

        return;

    }

    axios.delete(

        `${CUSTOMER_API}/${id}`,

        config

    )

    .then(() => {

        alert("Customer deleted successfully.");

        loadCustomers();

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

        // Unauthorized

        if (

            error.response.status === 401 ||

            error.response.status === 403

        ) {

            alert("Session expired. Please login again.");

            logout();

            return;

        }

        // Validation / Backend Message

        if (

            error.response.data &&

            error.response.data.message

        ) {

            alert(error.response.data.message);

            return;

        }

        // Other Errors

        alert(

            "Error : " +

            error.response.status

        );

    }

    else {

        alert("Server not reachable.");

    }

}