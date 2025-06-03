import React, { useState, useEffect } from 'react';
import { Card, Form } from 'react-bootstrap';
import { Line, Bar, Pie } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Tooltip,
  Legend,
} from 'chart.js';
import styles from './Analytics.module.css';
import { computePercentages } from '../../../utils/metricUtils';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend);

const Analytics = ({ metrics, filter, onChangeFilter }) => {

  const getDataByFilter = () => {
    return {
      revenue: {
        labels: metrics.revenue.labels,
        data: metrics.revenue.values,
      },
      topItems: {
        labels: metrics.topItems.labels,
        data: metrics.topItems.values,
      },
      categories: computePercentages(metrics.categories),
      avgOrderValue: 25.5,
    }
  };

  const data = getDataByFilter();

  const revenueChartData = {
    labels: data.revenue.labels,
    datasets: [
      {
        label: 'Revenue ($)',
        data: data.revenue.data,
        borderColor: '#2196f3',
        backgroundColor: 'rgba(33, 150, 243, 0.2)',
        fill: true,
      },
    ],
  };

  const topItemsChartData = {
    labels: data.topItems.labels,
    datasets: [
      {
        label: 'Units Sold',
        data: data.topItems.data,
        backgroundColor: ['#4caf50', '#ff9800', '#2196f3', '#f44336'],
      },
    ],
  };

  const categoriesChartData = {
    labels: data.categories.labels,
    datasets: [
      {
        data: data.categories.data,
        backgroundColor: ['#4caf50', '#ff9800', '#2196f3', '#f44336'],
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          font: {
            size: 12,
            family: "'Helvetica Neue', Arial, sans-serif",
          },
          color: '#555',
        },
      },
    },
  };

  return (
    <Card className={styles.card}>
      <Card.Body>
        <div className={styles.header}>
          <Card.Title className={styles.title}>Analytics</Card.Title>
          <Form.Select
            size="sm"
            value={filter}
            onChange={(e) => onChangeFilter(e.target.value)}
            className={styles.filter}
          >
            <option value="today">Today</option>
            <option value="week">This Week</option>
            <option value="month">This Month</option>
          </Form.Select>
        </div>
        <div className={styles.chartContainer}>
          <h6 className={styles.chartTitle}>Revenue Over Time</h6>
          <div className={styles.chart}>
            <Line data={revenueChartData} options={chartOptions} height={200} />
          </div>
        </div>
        <div className={styles.chartContainer}>
          <h6 className={styles.chartTitle}>Top-Selling Items</h6>
          <div className={styles.chart}>
            <Bar data={topItemsChartData} options={chartOptions} height={200} />
          </div>
        </div>
        <div className={styles.chartContainer}>
          <h6 className={styles.chartTitle}>Sales by Category</h6>
          <div className={styles.chart}>
            <Pie data={categoriesChartData} options={chartOptions} height={200} />
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

export default Analytics;