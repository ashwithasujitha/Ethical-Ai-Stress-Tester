export default function AffectedGroups({ groups }) {
  const list = Array.isArray(groups) ? groups : [];

  return (
    <div className="card table-card">
      <h3 className="table-card__title">Affected Groups</h3>

      {list.length === 0 ? (
        <p className="empty-note">No group-level disparities were flagged for this audit.</p>
      ) : (
        <table className="groups-table">
          <thead>
            <tr>
              <th>Metric</th>
              <th>Highest Group</th>
              <th>Lowest Group</th>
              <th>Gap</th>
            </tr>
          </thead>
          <tbody>
            {list.map((group, idx) => (
              <tr key={`${group.metric}-${idx}`}>
                <td>
                  <code>{group.metric}</code>
                </td>
                <td>{group.max_group}</td>
                <td>{group.min_group}</td>
                <td>{((Number(group.gap) || 0) * 100).toFixed(2)}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
