import React, { useEffect, useState } from "react";
import { Chart } from "react-google-charts";
import { getUserStatisticsDto, getTop10Countries } from '../api/statisticsApis';
import { FormControl, InputLabel, Select, MenuItem } from "@mui/material";

export default function AdminStatistics() {
    const [userStatisticsData, setUserStatisticsData] = useState(null);
    const [propertyStatisticsData, setPropertyStatisticsData] = useState(null);
    const [chartType, setChartType] = useState("pie");

    const handleChartTypeChange = (event) => {
        const selectedChartType = event.target.value;
        setChartType(selectedChartType);
    };

    useEffect(() => {
        fetchUserStatisticsData();
        fetchPropertiesStatisticsData();
    }, []);

    const fetchUserStatisticsData = async () => {
        try {
            const response = await getUserStatisticsDto();
            const { guestCount, hostCount } = response.data;
            setUserStatisticsData([["Task", "Count"], ["Guests", guestCount], ["Hosts", hostCount]]);
        } catch (error) {
            console.log(error);
        }
    };

    const fetchPropertiesStatisticsData = async () => {
        try {
            const response = await getTop10Countries();
            const topCountries = response.data;

            const statisticsData = [["Country", "Property Count"]];
            topCountries.forEach(country => {
                statisticsData.push([country.country, country.propertyCount]);
            });

            setPropertyStatisticsData(statisticsData);
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
                    data={propertyStatisticsData}
                    options={options}
                    width={"100%"}
                    height={"400px"}
                />
            );
        } else {
            return (
                <Chart
                    chartType="BarChart"
                    data={propertyStatisticsData}
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
                <h2>Distribution of User Roles</h2>
                {userStatisticsData ? (
                    <Chart
                        chartType="PieChart"
                        data={userStatisticsData}
                        options={options}
                        width={"100%"}
                        height={"400px"}
                    />
                ) : (
                    <p>Loading user statistics...</p>
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
                <h2>Top 10 Countries by Property Count</h2>
                {propertyStatisticsData ? (
                    renderChart()
                ) : (
                    <p>Loading property statistics...</p>
                )}
            </div>
        </div>
    );
}
