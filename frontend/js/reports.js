// ==========================================
// REPORTS API CONFIGURATION
// ==========================================

const DASHBOARD_API =
    `${API_BASE_URL}/api/dashboard`;


const token =
    localStorage.getItem("token");


if (!token) {

    window.location.href = "login.html";

}


const config = {

    headers: {

        Authorization: "Bearer " + token,

        "Content-Type": "application/json"

    }

};



let salesChart;
let popularChart;



// ==========================================
// PAGE LOAD
// ==========================================

document.addEventListener(
    "DOMContentLoaded",
    () => {

        loadReports();

    });




// ==========================================
// LOAD ALL REPORTS
// ==========================================


function loadReports() {


    loadSummary();

    loadSales();

    loadPopularItems();


}




// ==========================================
// SUMMARY DATA
// ==========================================


function loadSummary() {


    axios.get(

        `${DASHBOARD_API}/summary`,

        config

    )

        .then(response => {


            const data = response.data;



            document.getElementById("customersCount")
                .innerText =
                data.totalCustomers ?? 0;



            document.getElementById("ordersCount")
                .innerText =
                data.totalOrders ?? 0;



            document.getElementById("billsCount")
                .innerText =
                data.totalBills ?? 0;



            document.getElementById("revenue")
                .innerText =
                "₹ " + (data.totalRevenue ?? 0);



            document.getElementById("pendingOrders")
                .innerText =
                data.pendingOrders ?? 0;



            document.getElementById("completedOrders")
                .innerText =
                data.completedOrders ?? 0;



        })

        .catch(handleError);



}




// ==========================================
// SALES CHART
// ==========================================


function loadSales() {


    axios.get(

        `${DASHBOARD_API}/sales`,

        config

    )

        .then(response => {


            const data = response.data;



            const ctx =
                document
                    .getElementById("salesChart");



            if (salesChart) {

                salesChart.destroy();

            }



            salesChart = new Chart(

                ctx,

                {

                    type: "bar",

                    data: {

                        labels: [

                            "Total Sales"

                        ],


                        datasets: [{

                            label: "Revenue",

                            data: [

                                data.totalSales ??
                                data.totalRevenue ??
                                0

                            ]

                        }]

                    },


                    options: {

                        responsive: true,

                        plugins: {

                            legend: {

                                display: false

                            }

                        }

                    }

                }


            );



        })

        .catch(handleError);



}





// ==========================================
// POPULAR ITEMS
// ==========================================


function loadPopularItems() {


    axios.get(

        `${DASHBOARD_API}/popular-items`,

        config

    )

        .then(response => {


            const items = response.data;



            const table =
                document.getElementById(
                    "popularItemsTable"
                );



            table.innerHTML = "";



            let labels = [];

            let quantities = [];



            items.forEach(
                (item, index) => {


                    table.innerHTML += `

<tr>

<td>
${index + 1}
</td>

<td>
${item.menuItemName ?? item.name ?? "-"}
</td>

<td>
${item.quantitySold ?? item.quantity ?? 0}
</td>


</tr>

`;



                    labels.push(
                        item.menuItemName ?? item.name
                    );



                    quantities.push(
                        item.quantitySold ?? item.quantity ?? 0
                    );



                });



            createPopularChart(
                labels,
                quantities
            );



        })

        .catch(handleError);



}




// ==========================================
// POPULAR ITEMS CHART
// ==========================================


function createPopularChart(
    labels,
    data
) {



    const ctx =
        document
            .getElementById("popularChart");



    if (popularChart) {

        popularChart.destroy();

    }



    popularChart = new Chart(

        ctx,

        {

            type: "doughnut",


            data: {


                labels: labels,


                datasets: [{

                    label: "Quantity Sold",

                    data: data

                }]


            },


            options: {

                responsive: true

            }



        }



    );



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


            alert(
                "Session expired"
            );


            logout();


            return;


        }



        alert(
            "Error : " + error.response.status
        );



    }

    else {


        alert(
            "Server connection failed"
        );


    }



}