import {
	Chart as ChartJS,
	CategoryScale,
	LinearScale,
	PointElement,
	LineElement,
	ArcElement,
	BarElement,
	Tooltip,
	Legend,
} from "chart.js";
import { getCustomerIcon, getEarningIcon, getOrderIcon, getSaleIcon } from "../../../../services/ImageService";
import SalesTable from "../../../layouts/Chart/SalesTable";
import FullCalendar from "../../../layouts/Chart/Calendar";
import BarChart from "../../../layouts/Chart/BarChart";
import PieChart from "../../../layouts/Chart/PieChart";
import LineChart from "../../../layouts/Chart/LineChart";
import OrderNotificationsTable from "../../../layouts/Chart/OrderNotificationsTable";

ChartJS.register(
	CategoryScale,
	LinearScale,
	PointElement,
	LineElement,
	ArcElement,
	BarElement,
	Tooltip,
	Legend
);


export default function AdminDashboard() {
	const data = {
		analytics: {
			title: "Analytics Dashboard",
			stats: [
				{ title: "Sales", value: "2.382", change: "-3.65%", changeType: "danger", icon: getSaleIcon() },
				{ title: "Visitors", value: "14.212", change: "5.25%", changeType: "success", icon: getCustomerIcon() },
				{ title: "Earnings", value: "$21.300", change: "6.65%", changeType: "success", icon: getEarningIcon() },
				{ title: "Orders", value: "64", change: "-2.25%", changeType: "danger", icon: getOrderIcon() },
			],
		},
		recentMovement: {
			title: "Recent Movement",
			chartData: {
				labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
				datasets: [
					{
						label: "Visitors",
						data: [120, 190, 300, 500, 200, 300, 400],
						borderColor: "rgba(75, 192, 192, 1)",
						backgroundColor: "rgba(75, 192, 192, 0.2)",
					},
				],
			},
		},
		orders: {
			title: "Orders",
			chartData: {
				labels: ["Pending", "Successful", "Canceled"],
				datasets: [
					{
						data: [4306, 3801, 1689],
						backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
					},
				],
			},
		},
		monthlySales: {
			title: "Monthly Sales",
			chartData: {
				labels: ["January", "February", "March", "April", "May", "June"],
				datasets: [
					{
						label: "Sales",
						data: [65, 59, 80, 81, 56, 55],
						backgroundColor: "rgba(75, 192, 192, 0.2)",
						borderColor: "rgba(75, 192, 192, 1)",
						borderWidth: 1,
					},
				],
			},
		},
		salesData: [
			{
			  name: "Project Apollo",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Completed",
			  total_price: "$10,000",
			},
			{
			  name: "Project Fireball",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Cancelled",
			  total_price: "$8,500",
			},
			{
			  name: "Project Hades",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Completed",
			  total_price: "$12,000",
			},
			{
			  name: "Project Nitro",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Processing",
			  total_price: "$9,700",
			},
			{
			  name: "Project Phoenix",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Completed",
			  total_price: "$11,200",
			},
			{
			  name: "Project X",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Completed",
			  total_price: "$13,500",
			},
			{
			  name: "Project Romeo",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Completed",
			  total_price: "$7,800",
			},
			{
			  name: "Project Wombat",
			  date: "01/01/2023 - 31/06/2023",
			  status: "Processing",
			  total_price: "$10,300",
			},
		],
		
	};
	const orderNotifications = [
		{ id: "#1001", customer: "Alice Johnson", status: "Completed", time: "2 mins ago" },
		{ id: "#1002", customer: "Bob Williams", status: "Pending", time: "5 mins ago" },
		{ id: "#1003", customer: "Charlie Smith", status: "Cancelled", time: "10 mins ago" },
		{ id: "#1004", customer: "David Brown", status: "Shipped", time: "15 mins ago" },
		{ id: "#1005", customer: "Emma Davis", status: "Processing", time: "20 mins ago" },
	]

	return (
		<main className="content overflow-scroll px-5 py-3">
			<div className="container-fluid p-0">
				<h1 className="h3 my-3">
					<strong>{data.analytics.title}</strong>
				</h1>
				<div className="row">
					{data.analytics.stats.map((stat, index) => (
						<div className="col-sm-3" key={index}>
							<div className="card">
								<div className="card-body">
									<div className="row">
										<div className="col mt-0">
											<h5 className="card-title">{stat.title}</h5>
										</div>
										<div className="col-auto">
											<div className={`stat text-primary`}>
												<img className="dashboard-icon align-middle" src={stat.icon}/>
											</div>
										</div>
									</div>
									<h1 className="mt-1 mb-3">{stat.value}</h1>
									<div className="mb-0">
										<span className={`text-${stat.changeType}`}>{stat.change}</span>
										<span className="text-muted"> Since last week</span>
									</div>
								</div>
							</div>
						</div>
					))}
				</div>
				<div className="row">
					<div className="col-12 col-lg-8 col-xxl-8">
						<BarChart title={data.monthlySales.title} chartData={data.monthlySales.chartData}/>
					</div>

					<div className="col-12 col-lg-4 col-xxl-4 d-flex">
						<PieChart title={data.orders.title} chartData={data.orders.chartData}/>
					</div>	

					<div className="col-xl-7 col-xxl-7">
						<LineChart title={data.recentMovement.title} chartData={data.recentMovement.chartData}/>
					</div>		

					<div className="col-12 col-lg-5 col-xxl-5 d-flex">
						<FullCalendar/>
					</div>	
				</div>

				<div className="row">
					<div className="col-12 col-lg-7 col-xxl-7 d-flex">
						<SalesTable salesData={data.salesData}/>
					</div>
					<div className="col-12 col-lg-5 col-xxl-5 d-flex">
						<OrderNotificationsTable orderNotifications={orderNotifications}/>
					</div>
				</div>
			</div>
		</main>
	);
}
