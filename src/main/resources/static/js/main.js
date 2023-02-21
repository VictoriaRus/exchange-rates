"use strict"

window.addEventListener("load", main);

function main() {
    if (allRatesList) {

        let dropdownRates = document.getElementById("dpopdownRates");
        let rateID;

        dropdownRates.addEventListener("click", (event) => {
            let target = event.target;
            rateID = target.getAttribute('rateID');
            let rateAbbreviation = target.getAttribute('rateAbbreviation');
            let rateName = target.getAttribute('rateName');

            let dropdownButton = document.getElementById("dropdownButtonRates");
            dropdownButton.innerHTML = `${rateAbbreviation} ${rateName}`;
        })

        let dpopdownDateStart = document.getElementById("dpopdownDateStart");
        let rateDateStart;

        dpopdownDateStart.addEventListener("click", (event) => {
            let target = event.target;
            rateDateStart = target.getAttribute('date');

            let dropdownButton = document.getElementById("dropdownButtonDateStart");
            dropdownButton.innerHTML = `${rateDateStart}`;
        })

        let dpopdownDateEnd = document.getElementById("dpopdownDateEnd");
        let rateDateEnd;

        dpopdownDateEnd.addEventListener("click", (event) => {
            let target = event.target;
            rateDateEnd = target.getAttribute('date');

            let dropdownButton = document.getElementById("dropdownButtonDateEnd");
            dropdownButton.innerHTML = `${rateDateEnd}`;
        })

        let buttonPostToServer = document.getElementById("buttonPostToServer");

        buttonPostToServer.addEventListener("click", (event) => {
            postToServer(rateID, rateDateStart, rateDateEnd)
                .then((result) => {
                    printChart(result)
                })
        })
    }
}

async function postToServer(rateID, rateDateStart, rateDateEnd) {
    // Build formData object.
    let formData = new FormData();
    formData.append('code', rateID);
    formData.append('start', rateDateStart);
    formData.append('end', rateDateEnd);

    let fetchToServer = await fetch('http://127.0.0.1:9090/', {
        method: 'post',
        body: formData
    }).then((response) => {
        return response.json()
    }).then((rateSpecificDateDtoList) => {
        return rateSpecificDateDtoList;
    })
    return fetchToServer;
}

function printChart(rateSpecificDateDtoList) {
    let rates = [];
    let years = [];

    //shifting the desired values on the scales of chart
    rateSpecificDateDtoList.forEach(
        (rateSpecificDate) => {
            rates.push((parseInt(rateSpecificDate.Cur_OfficialRate)));
            //convert YYYY-MM-DD to YYYY
            years.push(rateSpecificDate.Date.substring(0, 4));
        }
    );

    let xValues = years;
    let yValues = rates;

    let yValuesMax = Math.max(...yValues);
    let yValuesMin = Math.min(...yValues);

    new Chart("myChart", {
        type: "line",
        data: {
            labels: xValues,
            datasets: [{
                fill: false,
                lineTension: 0,
                backgroundColor: "red",
                borderColor: "red",
                data: yValues
            }]
        },
        options: {
            legend: {display: false},
            scales: {
                y: [
                    {
                        ticks: {
                            min: (Math.round((yValuesMin) / 2)),
                            max: (Math.round(yValuesMax + (yValuesMax / 2)))
                        }
                    },
                ]
            },
            elements: {
                point: {
                    radius: 0
                }
            },
            tooltips: {
                mode: false,
                callbacks: {
                    title: function () {
                    },
                    label: function () {
                    }
                }
            },
            hover: {mode: null},
        }
    });
}