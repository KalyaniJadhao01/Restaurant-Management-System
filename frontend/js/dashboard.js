// ===============================
// Dashboard API Configuration
// ===============================

const API_URL = `${API_BASE_URL}/api/dashboard/summary`;


// ===============================
// Check Login
// ===============================

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}


// ===============================
// Axios Configuration
// ===============================

const axiosConfig = {

    headers: {
        Authorization: "Bearer " + token,
        "Content-Type": "application/json"
    }

};



// ===============================
// Load Dashboard Data
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    loadDashboard();

});



function loadDashboard(){


    axios
    .get(API_URL, axiosConfig)

    .then(response => {


        const data = response.data;


        document.getElementById("totalCustomers").innerText =
            data.totalCustomers ?? 0;


        document.getElementById("totalOrders").innerText =
            data.totalOrders ?? 0;


        document.getElementById("totalBills").innerText =
            data.totalBills ?? 0;


        document.getElementById("totalPayments").innerText =
            data.totalPayments ?? 0;



        document.getElementById("totalRevenue").innerText =
            "₹ " + formatNumber(data.totalRevenue);



        document.getElementById("pendingOrders").innerText =
            data.pendingOrders ?? 0;



        document.getElementById("completedOrders").innerText =
            data.completedOrders ?? 0;



    })

    .catch(error => {


        console.error("Dashboard Error:", error);


        if(error.response){


            if(error.response.status === 401 ||
               error.response.status === 403){


                alert("Session expired. Please login again.");

                logout();

                return;

            }

        }


        alert("Unable to load dashboard data.");

    });



}



// ===============================
// Number Formatting
// ===============================

function formatNumber(number){

    if(number === null || number === undefined){

        return "0";

    }


    return Number(number)
        .toLocaleString("en-IN");

}




// ===============================
// Logout
// ===============================

function logout(){


    localStorage.removeItem("token");


    window.location.href = "login.html";


}