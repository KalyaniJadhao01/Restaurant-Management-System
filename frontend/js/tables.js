// ======================================================
// Restaurant Management System
// Tables Module
// Part 1
// ======================================================

// ---------------- Base URL ----------------

const BASE_URL = "http://localhost:8080/api/tables";

// ---------------- JWT Token ----------------

const token = localStorage.getItem("token");

// ---------------- Axios Config ----------------

const axiosConfig = {

    headers: {

        Authorization: "Bearer " + token,

        "Content-Type": "application/json"

    }

};

// ---------------- DOM ----------------

const tableBody = document.getElementById("tableBody");

const tableModal = document.getElementById("tableModal");

const tableForm = document.getElementById("tableForm");

const modalTitle = document.getElementById("modalTitle");

const loadingOverlay = document.getElementById("loadingOverlay");

const toast = document.getElementById("toast");

const toastMessage = document.getElementById("toastMessage");

// Inputs

const tableId = document.getElementById("tableId");

const tableNumber = document.getElementById("tableNumber");

const capacity = document.getElementById("capacity");

const status = document.getElementById("status");

// Buttons

const addTableBtn = document.getElementById("addTableBtn");

const refreshBtn = document.getElementById("refreshBtn");

const availableBtn = document.getElementById("availableBtn");

const closeModalBtn = document.getElementById("closeModalBtn");

const cancelBtn = document.getElementById("cancelBtn");

const prevBtn = document.getElementById("prevBtn");

const nextBtn = document.getElementById("nextBtn");

// Search

const searchInput = document.getElementById("searchInput");

// Page Info

const pageInfo = document.getElementById("pageInfo");

// ---------------- Pagination ----------------

let currentPage = 0;

const pageSize = 10;

let totalPages = 1;

// ======================================================
// INITIALIZE
// ======================================================

document.addEventListener("DOMContentLoaded", () => {

    loadTables();

});

// ======================================================
// EVENT LISTENERS
// ======================================================

// Open Modal

addTableBtn.addEventListener("click", () => {

    resetForm();

    modalTitle.innerText = "Add Table";

    openModal();

});

// Close Modal

closeModalBtn.addEventListener("click", closeModal);

cancelBtn.addEventListener("click", closeModal);

// Submit Form

tableForm.addEventListener("submit", (e) => {

    e.preventDefault();

    saveTable();

});

// Refresh

refreshBtn.addEventListener("click", () => {

    loadTables();

});

// Available Tables

availableBtn.addEventListener("click", () => {

    loadAvailableTables();

});

// Search

searchInput.addEventListener("keyup", () => {

    filterTable();

});

// Pagination

prevBtn.addEventListener("click", () => {

    if (currentPage > 0) {

        currentPage--;

        loadTables();

    }

});

nextBtn.addEventListener("click", () => {

    if (currentPage < totalPages - 1) {

        currentPage++;

        loadTables();

    }

});

// ======================================================
// LOAD TABLES
// ======================================================

async function loadTables() {

    showLoading();

    try {

        const response = await axios.get(

            `${BASE_URL}?page=${currentPage}&size=${pageSize}&sortBy=id&direction=asc`,

            axiosConfig

        );

        const page = response.data;

        totalPages = page.totalPages;

        pageInfo.innerText =
            `Page ${currentPage + 1} of ${totalPages}`;

        renderTable(page.content);

    }

    catch (error) {

        console.error(error);

        showToast("Failed to load tables", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// LOAD AVAILABLE TABLES
// ======================================================

async function loadAvailableTables() {

    showLoading();

    try {

        const response = await axios.get(

            `${BASE_URL}/available`,

            axiosConfig

        );

        pageInfo.innerText = "Available Tables";

        renderTable(response.data);

    }

    catch (error) {

        console.error(error);

        showToast("Failed to load available tables", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// RENDER TABLE
// ======================================================

function renderTable(data) {

    tableBody.innerHTML = "";

    if (!data || data.length === 0) {

        tableBody.innerHTML = `

        <tr>

            <td colspan="5"
                style="text-align:center;padding:40px;">

                No tables found

            </td>

        </tr>`;

        return;

    }

    data.forEach(table => {

        tableBody.innerHTML += `

        <tr>

            <td>${table.id}</td>

            <td>${table.tableNumber}</td>

            <td>${table.capacity}</td>

            <td>

                ${statusBadge(table.status)}

            </td>

            <td>

                <div class="action-buttons">

                    <button
                        class="edit-btn"
                        onclick="editTable(${table.id})"
                        title="Edit">

                        <i class="fa-solid fa-pen"></i>

                    </button>

                    <button
                        class="status-btn"
                        onclick="changeStatus(${table.id})"
                        title="Change Status">

                        <i class="fa-solid fa-repeat"></i>

                    </button>

                    <button
                        class="delete-btn"
                        onclick="deleteTable(${table.id})"
                        title="Delete">

                        <i class="fa-solid fa-trash"></i>

                    </button>

                </div>

            </td>

        </tr>

        `;

    });

}



// ======================================================
// STATUS BADGE
// ======================================================

function statusBadge(status) {

    switch (status) {

        case "AVAILABLE":

            return `<span class="status available">
                        AVAILABLE
                    </span>`;

        case "OCCUPIED":

            return `<span class="status occupied">
                        OCCUPIED
                    </span>`;

        case "RESERVED":

            return `<span class="status reserved">
                        RESERVED
                    </span>`;

        default:

            return `<span class="status">
                        ${status}
                    </span>`;

    }

}



// ======================================================
// SEARCH
// ======================================================

function filterTable() {

    const value =
        searchInput.value.toLowerCase();

    const rows =
        tableBody.querySelectorAll("tr");

    rows.forEach(row => {

        row.style.display =
            row.innerText.toLowerCase().includes(value)
                ? ""
                : "none";

    });

}



// ======================================================
// MODAL
// ======================================================

function openModal() {

    tableModal.classList.add("show");

}

function closeModal() {

    tableModal.classList.remove("show");

    resetForm();

}



// ======================================================
// RESET FORM
// ======================================================

function resetForm() {

    tableForm.reset();

    tableId.value = "";

    status.value = "AVAILABLE";

}

// ======================================================
// SAVE TABLE (CREATE / UPDATE)
// ======================================================

async function saveTable() {

    const data = {

        tableNumber: tableNumber.value.trim(),

        capacity: Number(capacity.value),

        status: status.value

    };

    try {

        showLoading();

        if (tableId.value === "") {

            await axios.post(

                BASE_URL,

                data,

                axiosConfig

            );

            showToast("Table added successfully", true);

        }

        else {

            await axios.put(

                `${BASE_URL}/${tableId.value}`,

                data,

                axiosConfig

            );

            showToast("Table updated successfully", true);

        }

        closeModal();

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Unable to save table", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// EDIT TABLE
// ======================================================

async function editTable(id) {

    try {

        showLoading();

        const response = await axios.get(

            `${BASE_URL}/${id}`,

            axiosConfig

        );

        const table = response.data;

        tableId.value = table.id;

        tableNumber.value = table.tableNumber;

        capacity.value = table.capacity;

        status.value = table.status;

        modalTitle.innerText = "Edit Table";

        openModal();

    }

    catch (error) {

        console.error(error);

        showToast("Unable to load table", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// DELETE TABLE
// ======================================================

async function deleteTable(id) {

    const confirmDelete = confirm(

        "Delete this table?"

    );

    if (!confirmDelete) return;

    try {

        showLoading();

        await axios.delete(

            `${BASE_URL}/${id}`,

            axiosConfig

        );

        showToast("Table deleted", true);

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Delete failed", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// CHANGE STATUS
// ======================================================

async function changeStatus(id) {

    const nextStatus = prompt(

        "Enter Status:\nAVAILABLE\nOCCUPIED\nRESERVED"

    );

    if (!nextStatus) return;

    try {

        showLoading();

        await axios.patch(

            `${BASE_URL}/${id}/status?status=${nextStatus.toUpperCase()}`,

            {},

            axiosConfig

        );

        showToast("Status updated", true);

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Status update failed", false);

    }

}

// ======================================================
// SAVE TABLE (CREATE / UPDATE)
// ======================================================

async function saveTable() {

    const data = {

        tableNumber: tableNumber.value.trim(),

        capacity: Number(capacity.value),

        status: status.value

    };

    try {

        showLoading();

        if (tableId.value === "") {

            await axios.post(

                BASE_URL,

                data,

                axiosConfig

            );

            showToast("Table added successfully", true);

        }

        else {

            await axios.put(

                `${BASE_URL}/${tableId.value}`,

                data,

                axiosConfig

            );

            showToast("Table updated successfully", true);

        }

        closeModal();

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Unable to save table", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// EDIT TABLE
// ======================================================

async function editTable(id) {

    try {

        showLoading();

        const response = await axios.get(

            `${BASE_URL}/${id}`,

            axiosConfig

        );

        const table = response.data;

        tableId.value = table.id;

        tableNumber.value = table.tableNumber;

        capacity.value = table.capacity;

        status.value = table.status;

        modalTitle.innerText = "Edit Table";

        openModal();

    }

    catch (error) {

        console.error(error);

        showToast("Unable to load table", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// DELETE TABLE
// ======================================================

async function deleteTable(id) {

    const confirmDelete = confirm(

        "Delete this table?"

    );

    if (!confirmDelete) return;

    try {

        showLoading();

        await axios.delete(

            `${BASE_URL}/${id}`,

            axiosConfig

        );

        showToast("Table deleted", true);

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Delete failed", false);

    }

    finally {

        hideLoading();

    }

}



// ======================================================
// CHANGE STATUS
// ======================================================

async function changeStatus(id) {

    const nextStatus = prompt(

        "Enter Status:\nAVAILABLE\nOCCUPIED\nRESERVED"

    );

    if (!nextStatus) return;

    try {

        showLoading();

        await axios.patch(

            `${BASE_URL}/${id}/status?status=${nextStatus.toUpperCase()}`,

            {},

            axiosConfig

        );

        showToast("Status updated", true);

        loadTables();

    }

    catch (error) {

        console.error(error);

        showToast("Status update failed", false);

    }

}

// ======================================================
// LOADING
// ======================================================

function showLoading() {

    if (loadingOverlay) {

        loadingOverlay.classList.add("show");

    }

}

function hideLoading() {

    if (loadingOverlay) {

        loadingOverlay.classList.remove("show");

    }

}



// ======================================================
// TOAST MESSAGE
// ======================================================

function showToast(message, success = true) {

    if (!toast || !toastMessage) {

        alert(message);

        return;

    }

    toastMessage.innerText = message;

    toast.style.background = success
        ? "#16a34a"
        : "#dc2626";

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

    }, 3000);

}



// ======================================================
// CLOSE MODAL WHEN CLICKING OUTSIDE
// ======================================================

window.addEventListener("click", (event) => {

    if (event.target === tableModal) {

        closeModal();

    }

});



// ======================================================
// ENTER KEY SEARCH
// ======================================================

searchInput.addEventListener("keypress", (event) => {

    if (event.key === "Enter") {

        event.preventDefault();

        filterTable();

    }

});



// ======================================================
// ESC KEY CLOSE MODAL
// ======================================================

document.addEventListener("keydown", (event) => {

    if (event.key === "Escape") {

        closeModal();

    }

});



// ======================================================
// CHECK LOGIN
// ======================================================

if (!token) {

    alert("Please login first.");

    window.location.href = "login.html";

}



// ======================================================
// DISABLE PAGINATION BUTTONS
// ======================================================

function updatePaginationButtons() {

    prevBtn.disabled = currentPage <= 0;

    nextBtn.disabled = currentPage >= totalPages - 1;

}



// ======================================================
// OVERRIDE LOADTABLES TO UPDATE BUTTONS
// ======================================================

const originalLoadTables = loadTables;

loadTables = async function () {

    showLoading();

    try {

        const response = await axios.get(

            `${BASE_URL}?page=${currentPage}&size=${pageSize}&sortBy=id&direction=asc`,

            axiosConfig

        );

        const page = response.data;

        totalPages = page.totalPages;

        pageInfo.innerText = `Page ${currentPage + 1} of ${totalPages}`;

        renderTable(page.content);

        updatePaginationButtons();

    }

    catch (error) {

        console.error(error);

        showToast("Failed to load tables", false);

    }

    finally {

        hideLoading();

    }

};



// ======================================================
// INITIAL LOAD
// ======================================================

loadTables();