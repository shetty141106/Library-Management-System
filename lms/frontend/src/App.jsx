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
  Quantity: 1,
  publisherName: "",
  yearOfPublication: new Date().getFullYear()
};

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
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState("");
  const [error, setError] = useState("");

  const token = session?.accessToken;

  const filteredBooks = useMemo(() => {
    const term = query.trim().toLowerCase();
    if (!term) return books;
    return books.filter((book) =>
      [book.isbn, book.title, book.authName, book.category]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(term))
    );
  }, [books, query]);

  const stats = useMemo(() => {
    const categories = new Set(books.map((book) => book.category).filter(Boolean));
    const copies = books.reduce((total, book) => total + Number(book.Quantity || 0), 0);
    const value = books.reduce(
      (total, book) => total + Number(book.price || 0) * Number(book.Quantity || 0),
      0
    );
    return { titles: books.length, categories: categories.size, copies, value };
  }, [books]);

  useEffect(() => {
    if (session) {
      localStorage.setItem("lms-session", JSON.stringify(session));
      loadBooks();
    } else {
      localStorage.removeItem("lms-session");
    }
  }, [session]);

  async function request(path, options = {}) {
    const headers = {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers
    };
    return parseApiResponse(
      await fetch(path, {
        ...options,
        headers
      })
    );
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
      const response = await fetch(`/api/auth/${isLogin ? "login" : "register"}`, {
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
          Quantity: Number(bookForm.Quantity),
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

  async function deleteBook(isbn) {
    setLoading(true);
    setError("");
    setNotice("");
    try {
      const response = await request(`/api/books/${isbn}`, { method: "DELETE" });
      setNotice(response.message);
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
      publisherName: ""
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function logout() {
    setSession(null);
    setBooks([]);
    setNotice("");
    setError("");
  }

  if (!session) {
    return (
      <main className="auth-shell">
        <section className="auth-panel">
          <div>
            <p className="eyebrow">Library Management System</p>
            <h1>Manage your library catalog</h1>
            <p className="muted">
              Sign in or create a staff account to manage books from the Spring Boot backend.
            </p>
          </div>

          <div className="switcher" role="tablist" aria-label="Authentication mode">
            <button className={mode === "login" ? "active" : ""} onClick={() => setMode("login")}>
              Login
            </button>
            <button
              className={mode === "register" ? "active" : ""}
              onClick={() => setMode("register")}
            >
              Register
            </button>
          </div>

          <form onSubmit={handleAuth} className="form-grid">
            {mode === "register" && (
              <>
                <label>
                  Name
                  <input
                    value={register.name}
                    onChange={(event) => setRegister({ ...register, name: event.target.value })}
                    required
                  />
                </label>
                <label>
                  Address
                  <input
                    value={register.address}
                    onChange={(event) =>
                      setRegister({ ...register, address: event.target.value })
                    }
                    required
                  />
                </label>
                <label className="full">
                  Phone numbers
                  <input
                    value={register.phones}
                    onChange={(event) =>
                      setRegister({ ...register, phones: event.target.value })
                    }
                    placeholder="9876543210, 9123456780"
                    required
                  />
                </label>
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
      <header className="topbar">
        <div>
          <p className="eyebrow">Library Management System</p>
          <h1>Books Dashboard</h1>
        </div>
        <div className="user-actions">
          <span>{session.name || session.email}</span>
          <button onClick={logout}>Logout</button>
        </div>
      </header>

      <section className="stats-grid">
        <Stat label="Titles" value={stats.titles} />
        <Stat label="Copies" value={stats.copies} />
        <Stat label="Categories" value={stats.categories} />
        <Stat label="Stock value" value={`Rs. ${stats.value.toFixed(2)}`} />
      </section>

      <section className="workspace">
        <form onSubmit={saveBook} className="book-form">
          <div className="section-title">
            <h2>{editingIsbn ? "Update book" : "Add book"}</h2>
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
          <Field label="Quantity" name="Quantity" type="number" form={bookForm} setForm={setBookForm} />
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

        <section className="catalog">
          <div className="catalog-header">
            <div>
              <h2>Catalog</h2>
              <p className="muted">{filteredBooks.length} books shown</p>
            </div>
            <div className="toolbar">
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Search books"
              />
              <button onClick={loadBooks} disabled={loading}>
                Refresh
              </button>
            </div>
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
                {filteredBooks.map((book) => (
                  <tr key={book.isbn}>
                    <td>{book.isbn}</td>
                    <td>{book.title}</td>
                    <td>{book.authName}</td>
                    <td>{book.category}</td>
                    <td>{book.Quantity}</td>
                    <td>Rs. {Number(book.price || 0).toFixed(2)}</td>
                    <td className="row-actions">
                      <button onClick={() => startEdit(book)}>Edit</button>
                      <button className="danger" onClick={() => deleteBook(book.isbn)}>
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
    </main>
  );
}

function Field({ label, name, type = "text", form, setForm, disabled = false }) {
  return (
    <label>
      {label}
      <input
        type={type}
        value={form[name]}
        disabled={disabled}
        onChange={(event) => setForm({ ...form, [name]: event.target.value })}
        required={["isbn", "title", "authName", "category"].includes(name)}
      />
    </label>
  );
}

function Stat({ label, value }) {
  return (
    <article className="stat">
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}

function Status({ notice, error }) {
  if (!notice && !error) return null;
  return <p className={error ? "status error" : "status success"}>{error || notice}</p>;
}

export default App;
