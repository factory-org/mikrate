# Dialects

miKrate includes pluggable dialects that can be implemented according to the [API](./api).

## Supported dialects

Legend:

- ✅ = Full
- ↗ = External / 3rd party
- 🚧 = Partial support
- ⭕ = Not (yet) supported
- ⛔ = Intentionally unsupported

| DBMS | Core Support | Auto Migrate | Full JDBC Support | Full R2DBC Support | Comments |
| ---- | :----------: | :----------: | :---------------: | :----------------: | -------- |
| [Generic](./generic)     | ✅ | ⛔ | ⛔ | ⛔ | Only for visualization and testing |
| [SQLite](./sqlite)       | ✅ | ✅ | ✅ | ⭕<br>No Driver | No locking implemented (but should not be needed) |
| [PostgreSQL](./postgres) | ✅ | ✅ | ✅ | ✅ | |
