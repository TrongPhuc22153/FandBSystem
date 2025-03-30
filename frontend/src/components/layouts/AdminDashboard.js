import { Line, Pie, Bar } from "react-chartjs-2";
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
				{ title: "Sales", value: "2.382", change: "-3.65%", changeType: "danger", icon: "truck" },
				{ title: "Visitors", value: "14.212", change: "5.25%", changeType: "success", icon: "users" },
				{ title: "Earnings", value: "$21.300", change: "6.65%", changeType: "success", icon: "dollar-sign" },
				{ title: "Orders", value: "64", change: "-2.25%", changeType: "danger", icon: "shopping-cart" },
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
		browserUsage: {
			title: "Browser Usage",
			chartData: {
				labels: ["Chrome", "Firefox", "IE"],
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
	};

	return (
		<main className="content overflow-scroll">
			<div className="container-fluid p-0">
				<h1 className="h3 mb-3">
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
												<i className="align-middle" data-feather={stat.icon}></i>
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
					<div className="col-xl-7 col-xxl-7">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">{data.recentMovement.title}</h5>
							</div>
							<div className="card-body py-3">
								<Line data={data.recentMovement.chartData} />
							</div>
						</div>
					</div>
					<div className="col-12 col-lg-5 col-xxl-5">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">{data.monthlySales.title}</h5>
							</div>
							<div className="card-body d-flex w-100">
								<Bar data={data.monthlySales.chartData} />
							</div>
						</div>
					</div>

					<div className="col-12 col-md-8 col-xxl-8">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">{data.browserUsage.title}</h5>
							</div>
							<div className="card-body d-flex">
								<div className="align-self-center w-100">

								</div>
							</div>
						</div>
					</div>

					<div className="col-12 col-md-4 col-xxl-4">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">{data.browserUsage.title}</h5>
							</div>
							<div className="card-body d-flex">
								<div className="align-self-center w-100">
									<Pie data={data.browserUsage.chartData} />
								</div>
							</div>
						</div>
					</div>
				</div>
				<div className="row">
					{/* <div className="col-12 col-md-12 col-xxl-6 d-flex order-3 order-xxl-2">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">Real-Time</h5>
							</div>
							<div className="card-body px-4">
								<div id="world_map" style={{ height: "350px" }}></div>
							</div>
						</div>
					</div> */}
					<div className="col-12 col-md-6 col-xxl-6 d-flex order-1 order-xxl-1">
						<div className="card flex-fill">
							<div className="card-header">
								<h5 className="card-title mb-0">Calendar</h5>
							</div>
							<div className="card-body">


							</div>
						</div>
					</div>
				</div>
				<div className="row">
					<div className="col-12 col-lg-8 col-xxl-9 d-flex">
						<div className="card flex-fill">
							<div className="card-header">
								<h5 className="card-title mb-0">Latest Projects</h5>
							</div>
							<table className="table table-hover my-0">
								<thead>
									<tr>
										<th>Name</th>
										<th className="d-none d-xl-table-cell">Start Date</th>
										<th className="d-none d-xl-table-cell">End Date</th>
										<th>Status</th>
										<th className="d-none d-md-table-cell">Assignee</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Project Apollo</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-success">Done</span>
										</td>
										<td className="d-none d-md-table-cell">Vanessa Tucker</td>
									</tr>
									<tr>
										<td>Project Fireball</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-danger">Cancelled</span>
										</td>
										<td className="d-none d-md-table-cell">William Harris</td>
									</tr>
									<tr>
										<td>Project Hades</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-success">Done</span>
										</td>
										<td className="d-none d-md-table-cell">Sharon Lessman</td>
									</tr>
									<tr>
										<td>Project Nitro</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-warning">In progress</span>
										</td>
										<td className="d-none d-md-table-cell">Vanessa Tucker</td>
									</tr>
									<tr>
										<td>Project Phoenix</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-success">Done</span>
										</td>
										<td className="d-none d-md-table-cell">William Harris</td>
									</tr>
									<tr>
										<td>Project X</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-success">Done</span>
										</td>
										<td className="d-none d-md-table-cell">Sharon Lessman</td>
									</tr>
									<tr>
										<td>Project Romeo</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-success">Done</span>
										</td>
										<td className="d-none d-md-table-cell">Christina Mason</td>
									</tr>
									<tr>
										<td>Project Wombat</td>
										<td className="d-none d-xl-table-cell">01/01/2023</td>
										<td className="d-none d-xl-table-cell">31/06/2023</td>
										<td>
											<span className="badge bg-warning">In progress</span>
										</td>
										<td className="d-none d-md-table-cell">William Harris</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div className="col-12 col-lg-4 col-xxl-3 d-flex">
						<div className="card flex-fill w-100">
							<div className="card-header">
								<h5 className="card-title mb-0">Monthly Sales</h5>
							</div>
							<div className="card-body d-flex w-100">
								<div className="align-self-center chart chart-lg">
									<canvas id="chartjs-dashboard-bar"></canvas>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</main>
	);
}
