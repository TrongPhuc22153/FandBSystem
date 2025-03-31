import { Line } from "react-chartjs-2";

export default function LineChart({ title, chartData }){
    return(
        <div className="card flex-fill w-100">
            <div className="card-header">
                <h5 className="card-title mb-0">{title}</h5>
            </div>
            <div className="card-body py-3">
                <Line data={chartData} />
            </div>
        </div>
    )
}