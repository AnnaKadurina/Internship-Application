import React, { useEffect, useState } from "react";
import { Chart } from "react-google-charts";
import { getReviewStatisticsDTO, getTop10Properties } from '../api/statisticsApis';
import { FormControl, InputLabel, Select, MenuItem } from "@mui/material";

export default function AdminStatistics() {
    const [reviewStatisticsData, setReviewStatisticsData] = useState(null);
    const [propertiesStatisticsData, setPropertiesStatisticsData] = useState(null);
    const [chartType, setChartType] = useState("pie");

    const handleChartTypeChange = (event) => {
        const selectedChartType = event.target.value;
        setChartType(selectedChartType);
    };

    useEffect(() => {
        fetchReviewStatisticsData();
        fetchPropertiesStatisticsData();
    }, []);

    const fetchReviewStatisticsData = async () => {
        try {
            const response = await getReviewStatisticsDTO(localStorage.getItem('id'));
            const { positiveCount, negativeCount } = response.data;
            setReviewStatisticsData([["Task", "Count"], ["Positive (rating >= 5)", positiveCount], ["Negative (rating < 5)", negativeCount]]);
        } catch (error) {
            console.log(error);
        }
    };

    const fetchPropertiesStatisticsData = async () => {
        try {
            const response = await getTop10Properties(localStorage.getItem('id'));
            const topProperties = response.data;

            const statisticsData = [["Property", "Booking Count"]];
            topProperties.forEach(property => {
                statisticsData.push([property.propertyName, property.bookingCount]);
            });

            setPropertiesStatisticsData(statisticsData);
        } catch (error) {
            console.log(error);
        }
    };

    const options = {
        is3D: true,
        backgroundColor: "#D4DCDE",
        colors: ["#8E3B46", "#1C3144", "#615C99", "#4EA699", "#3D74B8", "#96705B", "#AB5F8D", "#594157", "#31572C", "#ADA20B"],
        chartArea: { width: "50%" },
        hAxis: {
            minValue: 0,
        }
    };

    const renderChart = () => {
        if (chartType === "pie") {
            return (
                <Chart
                    chartType="PieChart"
                    data={propertiesStatisticsData}
                    options={options}
                    width={"100%"}
                    height={"400px"}
                />
            );
        } else {
            return (
                <Chart
                    chartType="BarChart"
                    data={propertiesStatisticsData}
                    options={options}
                    width={"100%"}
                    height={"400px"}
                />
            );
        }
    };

    return (
        <div>
            <div>
                <h2>Distribution of Ratings: Positive vs. Negative</h2>
                {reviewStatisticsData ? (
                    <Chart
                        chartType="PieChart"
                        data={reviewStatisticsData}
                        options={options}
                        width={"100%"}
                        height={"400px"}
                    />
                ) : (
                    <p>Loading review statistics...</p>
                )}
            </div>
            <div>
                <FormControl sx={{ m: 1, minWidth: 120 }}>
                    <InputLabel htmlFor="chartType">Chart Type:</InputLabel>
                    <Select
                        id="chartType"
                        value={chartType}
                        onChange={handleChartTypeChange}
                        label="Chart Type"
                    >
                        <MenuItem value="pie">Pie Chart</MenuItem>
                        <MenuItem value="bar">Bar Chart</MenuItem>
                    </Select>
                </FormControl>
                <h2>Top 10 Properties by Booking Count</h2>
                {propertiesStatisticsData ? (
                    renderChart()
                ) : (
                    <p>Loading property statistics...</p>
                )}
            </div>
        </div>
    );
}
