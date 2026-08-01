// ===========================================
// BILL API CONFIGURATION
// ===========================================

const BILL_API = "http://localhost:8080/api/bills";

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

let currentBillId = null;

// ===========================================
// PAGE LOAD
// ===========================================

document.addEventListener("DOMContentLoaded", () => {

    loadBills();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchBills);

});

// ===========================================
// LOAD ALL BILLS
// ===========================================

function loadBills() {

    axios.get(BILL_API, config)

        .then(response => {

            const bills = response.data;

            const tbody =
                document.getElementById("billTableBody");

            tbody.innerHTML = "";

            bills.forEach(bill => {

                tbody.innerHTML += `

<tr>

<td>${bill.id}</td>

<td>#${bill.orderId}</td>

<td>₹ ${bill.subtotal}</td>

<td>₹ ${bill.taxAmount}</td>

<td>₹ ${bill.discountAmount}</td>

<td><strong>₹ ${bill.totalAmount}</strong></td>

<td>${statusBadge(bill.status)}</td>

<td>${new Date(bill.createdAt).toLocaleString()}</td>

<td>

<button
class="edit-btn"
title="Update Status"
onclick="editBill(${bill.id})">

Status

</button>

</td>

</tr>

`;

            });

        })

        .catch(handleError);

}

// ===========================================
// STATUS BADGES
// ===========================================

function statusBadge(status) {

    switch (status) {

        case "UNPAID":
            return `<span class="status unpaid">UNPAID</span>`;

        case "PAID":
            return `<span class="status paid">PAID</span>`;

        case "CANCELLED":
            return `<span class="status cancelled">CANCELLED</span>`;

        default:
            return status;
    }

}

// ===========================================
// SEARCH
// ===========================================

function searchBills() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#billTableBody tr")
        .forEach(row => {

            row.style.display =
                row.innerText
                    .toLowerCase()
                    .includes(value)
                    ? ""
                    : "none";

        });

}

// ===========================================
// OPEN MODAL
// ===========================================

function openModal() {

    currentBillId = null;

    document.getElementById("modalTitle").innerText =
        "Generate Bill";

    document.getElementById("orderId").value = "";

    document.getElementById("discount").value = 0;

    document.getElementById("billStatus").value =
        "UNPAID";

    document.getElementById("billModal").style.display =
        "flex";

}

// ===========================================
// CLOSE MODAL
// ===========================================

function closeModal() {

    document.getElementById("billModal").style.display =
        "none";

}


// ===========================================
// GENERATE BILL
// ===========================================

function generateBill() {

    const orderId =
        document.getElementById("orderId").value;

    const discount =
        document.getElementById("discount").value || 0;

    const status =
        document.getElementById("billStatus").value;

    if (!orderId) {

        alert("Please enter Order ID.");

        return;

    }

    axios.post(

        `${BILL_API}/generate/${orderId}?discount=${discount}`,

        {},

        config

    )

    .then(response => {

        const bill = response.data;

        // If user selected PAID/CANCELLED, update status

        if (status !== "UNPAID") {

            axios.patch(

                `${BILL_API}/${bill.id}/status?status=${status}`,

                {},

                config

            )

            .then(() => {

                alert("Bill generated successfully.");

                closeModal();

                loadBills();

            })

            .catch(handleError);

        } else {

            alert("Bill generated successfully.");

            closeModal();

            loadBills();

        }

    })

    .catch(handleError);

}

// ===========================================
// UPDATE BILL STATUS
// ===========================================

function editBill(id) {

    axios.get(

        `${BILL_API}/${id}`,

        config

    )

    .then(response => {

        const bill = response.data;

        const status = prompt(

`Enter Bill Status

UNPAID
PAID
CANCELLED`,

            bill.status

        );

        if (!status) {

            return;

        }

        axios.patch(

            `${BILL_API}/${id}/status?status=${status.toUpperCase()}`,

            {},

            config

        )

        .then(() => {

            alert("Bill status updated.");

            loadBills();

        })

        .catch(handleError);

    })

    .catch(handleError);

}

// ===========================================
// LOGOUT
// ===========================================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}

// ===========================================
// ERROR HANDLER
// ===========================================

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