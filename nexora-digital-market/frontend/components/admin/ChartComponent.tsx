interface ChartPoint {
  label: string;
  value: number;
}

interface ChartComponentProps {
  title: string;
  data: ChartPoint[];
}

export function ChartComponent({ title, data }: ChartComponentProps) {
  const max = Math.max(...data.map((d) => d.value), 1);

  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <h3 className="text-sm font-semibold text-slate-900">{title}</h3>
      <div className="mt-4 space-y-3">
        {data.map((point) => (
          <div key={point.label}>
            <div className="mb-1 flex justify-between text-xs text-slate-600">
              <span>{point.label}</span>
              <span>{point.value}</span>
            </div>
            <div className="h-2 rounded-full bg-slate-100">
              <div
                className="h-2 rounded-full bg-indigo-500"
                style={{ width: `${(point.value / max) * 100}%` }}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
