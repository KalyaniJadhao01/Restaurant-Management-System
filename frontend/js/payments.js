// ==========================================
// PAYMENT API CONFIGURATION
// ==========================================

const API_URL = `${API_BASE_URL}/api/payments`;

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

let currentPaymentId = null;

// ==========================================
// PAGE LOAD
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    loadPayments();

    document
        .getElementById("searchInput")
        .addEventListener("keyup", searchPayments);

});

// ==========================================
// LOAD PAYMENTS
// ==========================================

function loadPayments() {

    axios.get(API_URL, config)

        .then(response => {

            // Backend returns Page<PaymentResponse>
            const payments = response.data.content;

            const tbody =
                document.getElementById("paymentTableBody");

            tbody.innerHTML = "";

            payments.forEach(payment => {

                tbody.innerHTML += `

<tr>

<td>${payment.id}</td>

<td>${payment.billId}</td>

<td>₹ ${payment.amount}</td>

<td>${payment.paymentMethod}</td>

<td>${statusBadge(payment.status)}</td>

<td>${payment.transactionReference ?? "-"}</td>

<td>${formatDate(payment.createdAt)}</td>

<td>

<button
class="edit-btn"
title="Update Payment Status"
onclick="updateStatus(${payment.id})">

<i class="fa-solid fa-pen"></i>

</button>

</td>

</tr>

`;

            });

        })

        .catch(handleError);

}

// ==========================================
// STATUS BADGE
// ==========================================

function statusBadge(status) {

    switch (status) {

        case "PENDING":
            return `<span class="status pending">PENDING</span>`;

        case "SUCCESS":
            return `<span class="status success">SUCCESS</span>`;

        case "FAILED":
            return `<span class="status failed">FAILED</span>`;

        case "REFUNDED":
            return `<span class="status refunded">REFUNDED</span>`;

        default:
            return status;
    }

}

// ==========================================
// SEARCH
// ==========================================

function searchPayments() {

    const value =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase();

    document
        .querySelectorAll("#paymentTableBody tr")
        .forEach(row => {

            row.style.display =
                row.innerText
                    .toLowerCase()
                    .includes(value)
                    ? ""
                    : "none";

        });

}

// ==========================================
// OPEN MODAL
// ==========================================

function openModal() {

    currentPaymentId = null;

    document.getElementById("modalTitle").innerText =
        "New Payment";

    document.getElementById("billId").value = "";

    document.getElementById("amount").value = "";

    document.getElementById("paymentMethod").value = "CASH";

    document.getElementById("paymentStatus").value = "PENDING";

    document.getElementById("paymentModal").style.display =
        "flex";

}

// ==========================================
// CLOSE MODAL
// ==========================================

function closeModal() {

    document.getElementById("paymentModal").style.display =
        "none";

}

// ==========================================
// FORMAT DATE
// ==========================================

function formatDate(date) {

    if (!date) return "-";

    return new Date(date).toLocaleString();

}


// ==========================================
// SAVE PAYMENT
// ==========================================

function savePayment() {

    const payment = {

        billId: Number(
            document.getElementById("billId").value
        ),

        amount: Number(
            document.getElementById("amount").value
        ),

        paymentMethod:
            document.getElementById("paymentMethod").value

    };

    const selectedStatus =
        document.getElementById("paymentStatus").value;

    axios.post(

        API_URL,

        payment,

        config

    )

    .then(response => {

        const createdPayment = response.data;

        // Update status if user selected something other than default
        if (selectedStatus !== "PENDING") {

            axios.patch(

                `${API_URL}/${createdPayment.id}/status?status=${selectedStatus}`,

                {},

                config

            )

            .then(() => {

                alert("Payment created successfully.");

                closeModal();

                loadPayments();

            })

            .catch(handleError);

        } else {

            alert("Payment created successfully.");

            closeModal();

            loadPayments();

        }

    })

    .catch(handleError);

}

// ==========================================
// UPDATE STATUS
// ==========================================

function updateStatus(id) {

    const status = prompt(

`Enter Payment Status

PENDING
SUCCESS
FAILED
REFUNDED`

    );

    if (!status) return;

    axios.patch(

        `${API_URL}/${id}/status?status=${status.toUpperCase()}`,

        {},

        config

    )

    .then(() => {

        alert("Payment status updated.");

        loadPayments();

    })

    .catch(handleError);

}

// ==========================================
// LOGOUT
// ==========================================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}

// ==========================================
// ERROR HANDLER
// ==========================================

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