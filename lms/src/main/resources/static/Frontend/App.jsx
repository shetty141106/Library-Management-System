import { useEffect, useMemo, useState } from "react";

const emptyLogin = { email: "", password: "" };
const emptyRegister = {
  name: "",
  email: "",
  password: "",
  confirm_password: "",
  address: "",
  phones: ""
};
const emptyBook = {
  isbn: "",
  confirm_isbn: "",
  title: "",
  edition: 1,
  authName: "",
  price: 0,
  category: "",
  quantity: 1,
  publisherName: "",
  yearOfPublication: new Date().getFullYear()
};

const API_URL =
  import.meta.env.VITE_API_URL || "https://library-management-system-0g2d.onrender.com";

function getStoredSession() {
  try {
    return JSON.parse(localStorage.getItem("lms-session")) || null;
  } catch {
    return null;
  }
}

async function parseApiResponse(response) {
  const payload = await response.json().catch(() => null);
  if (!response.ok || payload?.success === false) {
    throw new Error(payload?.message || "Request failed.");
  }
  return payload;
}

function App() {
  const [session, setSession] = useState(getStoredSession);
  const [mode, setMode] = useState("login");
  const [login, setLogin] = useState(emptyLogin);
  const [register, setRegister] = useState(emptyRegister);
  const [books, setBooks] = useState([]);
  const [bookForm, setBookForm] = useState(emptyBook);
  const [editingIsbn, setEditingIsbn] = useState("");
  const [query, setQuery] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");
  const [sortBy, setSortBy] = useState("title");
  const [pendingDelete, setPendingDelete] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState("");
  const [error, setError] = useState("");

  const token = session?.accessToken;

  const categories = useMemo(
    () => ["All", ...new Set(books.map((book) => book.category).filter(Boolean))],
    [books]
  );

  const filteredBooks = useMemo(() => {
    const term = query.trim().toLowerCase();
    return books
      .filter((book) => activeCategory === "All" || book.category === activeCategory)
      .filter((book) => {
        if (!term) return true;
        return [book.isbn, book.title, book.authName, book.category, book.quantity, book.publisherName]
          .filter(Boolean)
          .some((value) => String(value).toLowerCase().includes(term));
      })
      .sort((a, b) => {
        if (sortBy === "quantity") return Number(b.quantity || 0) - Number(a.quantity || 0);
        if (sortBy === "price") return Number(b.price || 0) - Number(a.price || 0);
        if (sortBy === "year") {
          return Number(b.yearOfPublication || 0) - Number(a.yearOfPublication || 0);
        }
        return String(a.title || "").localeCompare(String(b.title || ""));
      });
  }, [activeCategory, books, query, sortBy]);

  const stats = useMemo(() => {
    const categorySet = new Set(books.map((book) => book.category).filter(Boolean));
    const copies = books.reduce((total, book) => total + Number(book.quantity || 0), 0);
    const value = books.reduce(
      (total, book) => total + Number(book.price || 0) * Number(book.quantity || 0),
      0
    );
    const averagePrice = books.length ? value / Math.max(copies, 1) : 0;
    return { titles: books.length, categories: categorySet.size, copies, value, averagePrice };
  }, [books]);

  useEffect(() => {
    if (session) {
      localStorage.setItem("lms-session", JSON.stringify(session));
      loadBooks();
    } else {
      localStorage.removeItem("lms-session");
    }
  }, [session]);

  useEffect(() => {
    if (!categories.includes(activeCategory)) {
      setActiveCategory("All");
    }
  }, [activeCategory, categories]);

  async function request(path, options = {}) {
    const headers = {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers
    };
    try {
          return await parseApiResponse(
            await fetch(`${API_URL}${path}`, {
              ...options,
              headers
            })
          );
        } catch (err) {
          if (err.message.includes("JWT expired")) {
            logout("Session expired. Please login again.");
            throw new Error("Session expired. Please login again.");
          }
          throw err;
        }
  }

  async function loadBooks() {
    if (!token) return;
    setLoading(true);
    setError("");
    try {
      const response = await request("/api/books");
      setBooks(response.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleAuth(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    setNotice("");
    try {
      const isLogin = mode === "login";
      const body = isLogin
        ? login
        : {
            ...register,
            phones: register.phones
              .split(",")
              .map((phone) => phone.trim())
              .filter(Boolean)
          };
      const response = await fetch(`${API_URL}/api/auth/${isLogin ? "login" : "register"}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      }).then(parseApiResponse);

      setSession(response.data);
      setNotice(response.message);
      setLogin(emptyLogin);
      setRegister(emptyRegister);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function saveBook(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    setNotice("");
    try {
      const method = editingIsbn ? "PUT" : "POST";
      const path = editingIsbn ? `/api/books/${editingIsbn}` : "/api/books";
      const response = await request(path, {
        method,
        body: JSON.stringify({
          ...bookForm,
          confirm_isbn: bookForm.confirm_isbn || bookForm.isbn,
          edition: Number(bookForm.edition),
          price: Number(bookForm.price),
          quantity: Number(bookForm.quantity),
          yearOfPublication: Number(bookForm.yearOfPublication)
        })
      });
      setNotice(response.message);
      setBookForm(emptyBook);
      setEditingIsbn("");
      await loadBooks();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function deleteBook(book) {
    setLoading(true);
    setError("");
    setNotice("");
    try {
      const response = await request(`/api/books/${book.isbn}`, { method: "DELETE" });
      setNotice(response.message);
      setPendingDelete(null);
      await loadBooks();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function startEdit(book) {
    setEditingIsbn(book.isbn);
    setBookForm({
      ...emptyBook,
      ...book,
      confirm_isbn: book.isbn,
      publisherName: book.publisherName || ""
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function logout(message="") {
    setSession(null);
    setBooks([]);
    setNotice("");
    setError(message);
  }

  if (!session) {
    return (
      <main className="auth-shell">
        <section className="auth-panel entrance">
          <div className="brand-mark" aria-hidden="true">
            <span>LM</span>
          </div>
          <div>
            <p className="eyebrow">Library Management System</p>
            <h1>Manage your library catalog</h1>
            <p className="muted">
              Sign in or create a staff account to keep titles, stock, and categories organized.
            </p>
          </div>

          <div className="auth-highlights" aria-label="Catalog highlights">
            <span>Fast catalog edits</span>
            <span>Live inventory</span>
            <span>Secure staff access</span>
          </div>

          <div className="switcher" role="tablist" aria-label="Authentication mode">
            <button
              className={mode === "login" ? "active" : ""}
              onClick={() => setMode("login")}
              type="button"
            >
              Login
            </button>
            <button
              className={mode === "register" ? "active" : ""}
              onClick={() => setMode("register")}
              type="button"
            >
              Register
            </button>
          </div>

          <form onSubmit={handleAuth} className="form-grid">
            {mode === "register" && (
              <>
                <Field
                  label="Name"
                  name="name"
                  form={register}
                  setForm={setRegister}
                  autoComplete="name"
                  required
                />
                <Field label="Address" name="address" form={register} setForm={setRegister} required />
                <Field
                  className="full"
                  label="Phone numbers"
                  name="phones"
                  form={register}
                  setForm={setRegister}
                  placeholder="9876543210, 9123456780"
                  required
                />
              </>
            )}

            <label className={mode === "login" ? "full" : ""}>
              Email
              <input
                type="email"
                value={mode === "login" ? login.email : register.email}
                onChange={(event) =>
                  mode === "login"
                    ? setLogin({ ...login, email: event.target.value })
                    : setRegister({ ...register, email: event.target.value })
                }
                autoComplete="email"
                required
              />
            </label>
            <label>
              Password
              <input
                type="password"
                value={mode === "login" ? login.password : register.password}
                onChange={(event) =>
                  mode === "login"
                    ? setLogin({ ...login, password: event.target.value })
                    : setRegister({ ...register, password: event.target.value })
                }
                autoComplete={mode === "login" ? "current-password" : "new-password"}
                required
              />
            </label>
            {mode === "register" && (
              <label>
                Confirm password
                <input
                  type="password"
                  value={register.confirm_password}
                  onChange={(event) =>
                    setRegister({ ...register, confirm_password: event.target.value })
                  }
                  autoComplete="new-password"
                  required
                />
              </label>
            )}

            <button className="primary full" disabled={loading}>
              {loading ? "Please wait..." : mode === "login" ? "Login" : "Create account"}
            </button>
          </form>
          <Status notice={notice} error={error} />
        </section>
      </main>
    );
  }

  return (
    <main className="app-shell">
      {loading && <div className="loading-bar" aria-hidden="true" />}
      <header className="topbar entrance">
        <div>
          <p className="eyebrow">Library Management System</p>
          <h1>Books Dashboard</h1>
        </div>
        <div className="user-actions">
          <span>{session.name || session.email}</span>
          <button onClick={logout}>Logout</button>
        </div>
      </header>

      <section className="stats-grid" aria-label="Catalog summary">
        <Stat label="Titles" value={stats.titles} tone="teal" />
        <Stat label="Copies" value={stats.copies} tone="gold" />
        <Stat label="Categories" value={stats.categories} tone="blue" />
        <Stat label="Avg. price" value={`Rs. ${stats.averagePrice.toFixed(2)}`} tone="rose" />
      </section>

      <section className="workspace">
        <form onSubmit={saveBook} className="book-form surface">
          <div className="section-title">
            <div>
              <p className="eyebrow">{editingIsbn ? "Editing" : "Catalog entry"}</p>
              <h2>{editingIsbn ? "Update book" : "Add book"}</h2>
            </div>
            {editingIsbn && (
              <button
                type="button"
                onClick={() => {
                  setEditingIsbn("");
                  setBookForm(emptyBook);
                }}
              >
                Cancel
              </button>
            )}
          </div>

          <Field label="ISBN" name="isbn" form={bookForm} setForm={setBookForm} disabled={!!editingIsbn} />
          <Field label="Confirm ISBN" name="confirm_isbn" form={bookForm} setForm={setBookForm} />
          <Field label="Title" name="title" form={bookForm} setForm={setBookForm} />
          <Field label="Author" name="authName" form={bookForm} setForm={setBookForm} />
          <Field label="Category" name="category" form={bookForm} setForm={setBookForm} />
          <Field label="Publisher" name="publisherName" form={bookForm} setForm={setBookForm} />
          <Field label="Edition" name="edition" type="number" form={bookForm} setForm={setBookForm} />
          <Field label="Price" name="price" type="number" form={bookForm} setForm={setBookForm} />
          <Field label="Quantity" name="quantity" type="number" form={bookForm} setForm={setBookForm} />
          <Field
            label="Publication year"
            name="yearOfPublication"
            type="number"
            form={bookForm}
            setForm={setBookForm}
          />

          <button className="primary" disabled={loading}>
            {editingIsbn ? "Save changes" : "Add book"}
          </button>
        </form>

        <section className="catalog surface">
          <div className="catalog-header">
            <div>
              <p className="eyebrow">Inventory</p>
              <h2>Catalog</h2>
              <p className="muted">{filteredBooks.length} books shown</p>
            </div>
            <div className="toolbar">
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Search by title, ISBN, author"
              />
              <select value={sortBy} onChange={(event) => setSortBy(event.target.value)}>
                <option value="title">Title</option>
                <option value="quantity">Quantity</option>
                <option value="price">Price</option>
                <option value="year">Newest</option>
              </select>
              <button onClick={loadBooks} disabled={loading}>
                Refresh
              </button>
            </div>
          </div>

          <div className="category-strip" aria-label="Filter by category">
            {categories.map((category) => (
              <button
                key={category}
                className={category === activeCategory ? "active" : ""}
                type="button"
                onClick={() => setActiveCategory(category)}
              >
                {category}
              </button>
            ))}
          </div>

          <Status notice={notice} error={error} />

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ISBN</th>
                  <th>Title</th>
                  <th>Author</th>
                  <th>Category</th>
                  <th>Qty</th>
                  <th>Price</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {filteredBooks.map((book, index) => (
                  <tr key={book.isbn} style={{ animationDelay: `${index * 35}ms` }}>
                    <td>{book.isbn}</td>
                    <td>
                      <strong>{book.title}</strong>
                      <span className="table-subtext">{book.publisherName}</span>
                    </td>
                    <td>{book.authName}</td>
                    <td>
                      <span className="tag">{book.category || "Unsorted"}</span>
                    </td>
                    <td>{book.quantity}</td>
                    <td>Rs. {Number(book.price || 0).toFixed(2)}</td>
                    <td className="row-actions">
                      <button onClick={() => startEdit(book)}>Edit</button>
                      <button className="danger" onClick={() => setPendingDelete(book)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
                {!filteredBooks.length && (
                  <tr>
                    <td colSpan="7" className="empty">
                      {loading ? "Loading catalog..." : "No books found."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      {pendingDelete && (
        <div className="modal-backdrop" role="presentation" onClick={() => setPendingDelete(null)}>
          <section
            className="confirm-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="delete-title"
            onClick={(event) => event.stopPropagation()}
          >
            <p className="eyebrow">Confirm delete</p>
            <h2 id="delete-title">{pendingDelete.title}</h2>
            <p className="muted">This removes ISBN {pendingDelete.isbn} from the catalog.</p>
            <div className="modal-actions">
              <button onClick={() => setPendingDelete(null)} type="button">
                Keep book
              </button>
              <button className="primary danger-primary" onClick={() => deleteBook(pendingDelete)}>
                Delete book
              </button>
            </div>
          </section>
        </div>
      )}
    </main>
  );
}

function Field({
  label,
  name,
  type = "text",
  form,
  setForm,
  disabled = false,
  className = "",
  required,
  ...props
}) {
  return (
    <label className={className}>
      {label}
      <input
        type={type}
        value={form[name]}
        disabled={disabled}
        onChange={(event) => setForm({ ...form, [name]: event.target.value })}
        required={required ?? ["isbn", "title", "authName", "category"].includes(name)}
        {...props}
      />
    </label>
  );
}

function Stat({ label, value, tone }) {
  return (
    <article className={`stat ${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}

function Status({ notice, error }) {
  if (!notice && !error) return null;
  return (
    <p className={error ? "status error" : "status success"} role="status">
      {error || notice}
    </p>
  );
}

export default App;
