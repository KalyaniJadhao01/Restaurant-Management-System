// ======================================
// API CONFIGURATION
// ======================================

const ORDER_API = `${API_BASE_URL}/api/orders`;
const CUSTOMER_API =`${API_BASE_URL}/api/customers`;
const TABLE_API = `${API_BASE_URL}/api/tables/available`;
const MENU_API = `${API_BASE_URL}/api/menu-items/available`

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

let selectedItems = [];
let editOrderId = null;

// ======================================
// PAGE LOAD
// ======================================

document.addEventListener("DOMContentLoaded", () => {

    loadOrders();

    loadCustomers();

    loadTables();

    loadMenuItems();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchOrders);

});

// ======================================
// LOAD ORDERS
// ======================================

function loadOrders() {

    axios.get(ORDER_API, config)

        .then(response => {

            const orders = response.data.content;

            const tbody =
                document.getElementById("ordersTableBody");

            tbody.innerHTML = "";

            orders.forEach(order => {

                tbody.innerHTML += `

<tr>

<td>${order.id}</td>

<td>${order.customerName}</td>

<td>${order.tableNumber}</td>

<td>₹ ${order.totalAmount}</td>

<td>${statusBadge(order.status)}</td>

<td>${new Date(order.createdAt).toLocaleString()}</td>

<td>

<button
class="edit-btn"
onclick="editOrder(${order.id})">

Status

</button>

<button
class="delete-btn"
onclick="deleteOrder(${order.id})">

Delete

</button>

</td>

</tr>

`;

            });

        })

        .catch(handleError);

}

// ======================================
// STATUS BADGE
// ======================================

function statusBadge(status) {

    switch (status) {

        case "PENDING":
            return '<span class="status pending">Pending</span>';

        case "PREPARING":
            return '<span class="status preparing">Preparing</span>';

        case "SERVED":
            return '<span class="status served">Served</span>';

        case "COMPLETED":
            return '<span class="status completed">Completed</span>';

        case "CANCELLED":
            return '<span class="status cancelled">Cancelled</span>';

        default:
            return status;
    }

}

// ======================================
// LOAD CUSTOMERS
// ======================================

function loadCustomers() {

    axios.get(CUSTOMER_API, config)

        .then(response => {

            const customers = response.data.content;

            const select =
                document.getElementById("customerSelect");

            select.innerHTML = "";

            customers.forEach(customer => {

                select.innerHTML += `

<option value="${customer.id}">

${customer.name}

</option>

`;

            });

        })

        .catch(handleError);

}

// ======================================
// LOAD TABLES
// ======================================

function loadTables() {

    axios.get(TABLE_API, config)

        .then(response => {

            const tables = response.data;

            const select =
                document.getElementById("tableSelect");

            select.innerHTML = "";

            tables.forEach(table => {

                select.innerHTML += `

<option value="${table.id}">

${table.tableNumber}

(Capacity ${table.capacity})

</option>

`;

            });

        })

        .catch(handleError);

}

// ======================================
// LOAD MENU ITEMS
// ======================================

function loadMenuItems() {

    axios.get(MENU_API, config)

        .then(response => {

            const items = response.data;

            const select =
                document.getElementById("menuItemSelect");

            select.innerHTML = "";

            items.forEach(item => {

                select.innerHTML += `

<option
value="${item.id}"
data-price="${item.price}">

${item.name}

- ₹${item.price}

</option>

`;

            });

        })

        .catch(handleError);

}

// ======================================
// SEARCH
// ======================================

function searchOrders() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#ordersTableBody tr")
        .forEach(row => {

            row.style.display =
                row.innerText
                    .toLowerCase()
                    .includes(value)
                    ? ""
                    : "none";

        });

}

// ======================================
// OPEN MODAL
// ======================================

function openModal() {

    editOrderId = null;

    selectedItems = [];

    document.getElementById("modalTitle").innerText =
        "Create Order";

    document.getElementById("selectedItemsBody").innerHTML = "";

    document.getElementById("grandTotal").innerHTML =
        "₹ 0.00";

    document.getElementById("orderModal").style.display =
        "flex";

}

// ======================================
// CLOSE MODAL
// ======================================

function closeModal() {

    document.getElementById("orderModal").style.display =
        "none";

}

// ======================================
// ADD ITEM
// ======================================

function addItem() {

    const menuSelect =
        document.getElementById("menuItemSelect");

    const quantity =
        parseInt(document.getElementById("quantity").value);

    if (quantity <= 0) {

        alert("Quantity must be at least 1.");

        return;

    }

    const option =
        menuSelect.options[menuSelect.selectedIndex];

    const item = {

        menuItemId: Number(option.value),

        menuItemName: option.text.split("- ₹")[0].trim(),

        price: Number(option.dataset.price),

        quantity: quantity

    };

    const existing =
        selectedItems.find(i => i.menuItemId === item.menuItemId);

    if (existing) {

        existing.quantity += quantity;

    } else {

        selectedItems.push(item);

    }

    renderItems();

}

// ======================================
// REMOVE ITEM
// ======================================

function removeItem(index) {

    selectedItems.splice(index, 1);

    renderItems();

}

// ======================================
// RENDER ITEMS
// ======================================

function renderItems() {

    const tbody =
        document.getElementById("selectedItemsBody");

    tbody.innerHTML = "";

    let total = 0;

    selectedItems.forEach((item, index) => {

        const subtotal =
            item.price * item.quantity;

        total += subtotal;

        tbody.innerHTML += `

<tr>

<td>${item.menuItemName}</td>

<td>${item.quantity}</td>

<td>₹ ${item.price}</td>

<td>₹ ${subtotal.toFixed(2)}</td>

<td>

<button
class="delete-btn"
title="Remove Item"
aria-label="Remove Item"
onclick="removeItem(${index})">

<i class="fa-solid fa-trash"></i>

</button>

</td>

</tr>

`;

    });

    document.getElementById("grandTotal").innerHTML =
        "₹ " + total.toFixed(2);

}

// ======================================
// BUILD REQUEST BODY
// ======================================

function buildRequest() {

    return {

        customerId: Number(
            document.getElementById("customerSelect").value
        ),

        tableId: Number(
            document.getElementById("tableSelect").value
        ),

        items: selectedItems.map(item => ({

            menuItemId: item.menuItemId,

            quantity: item.quantity

        }))

    };

}

// ======================================
// VALIDATION
// ======================================

function validateOrder() {

    if (selectedItems.length === 0) {

        alert("Please add at least one menu item.");

        return false;

    }

    return true;

}


// ======================================
// SAVE ORDER
// ======================================

function saveOrder() {

    if (!validateOrder()) {
        return;
    }

    const request = buildRequest();

    axios.post(
        ORDER_API,
        request,
        config
    )

    .then(() => {

        alert("Order created successfully.");

        closeModal();

        loadOrders();

    })

    .catch(handleError);

}

// ======================================
// EDIT ORDER (STATUS ONLY)
// ======================================

function editOrder(id) {

    axios.get(

        `${ORDER_API}/${id}`,

        config

    )

    .then(response => {

        const order = response.data;

        const status = prompt(

            "Enter Status:\n\nPENDING\nPREPARING\nSERVED\nCOMPLETED\nCANCELLED",

            order.status

        );

        if (!status) {
            return;
        }

        axios.patch(

            `${ORDER_API}/${id}/status?status=${status.toUpperCase()}`,

            {},

            config

        )

        .then(() => {

            alert("Order status updated.");

            loadOrders();

        })

        .catch(handleError);

    })

    .catch(handleError);

}

// ======================================
// DELETE ORDER
// ======================================

function deleteOrder(id) {

    if (!confirm("Delete this order?")) {
        return;
    }

    axios.delete(

        `${ORDER_API}/${id}`,

        config

    )

    .then(() => {

        alert("Order deleted successfully.");

        loadOrders();

    })

    .catch(handleError);

}

// ======================================
// LOGOUT
// ======================================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}

// ======================================
// ERROR HANDLER
// ======================================

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

        alert("Error : " + error.response.status);

        return;
    }

    alert("Unable to connect to server.");

}