/** API client — uses local Java server when available, falls back gracefully. */
const API_BASE = (typeof window !== 'undefined' && window.location.port === '8080')
  ? ''
  : (localStorage.getItem('apiBase') || 'http://localhost:8080');

async function api(path, options = {}) {
  const url = API_BASE + path;
  const opts = {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  };
  try {
    const res = await fetch(url, opts);
    const data = await res.json();
    return data;
  } catch (e) {
    return {
      success: false,
      message: 'Cannot reach Java API at ' + url + '. Start the local server (see README). ' + (e.message || '')
    };
  }
}

function getUser() {
  try { return JSON.parse(localStorage.getItem('user') || 'null'); } catch { return null; }
}

function setUser(user) {
  if (user) localStorage.setItem('user', JSON.stringify(user));
  else localStorage.removeItem('user');
}

function requireAuth(roles) {
  const user = getUser();
  if (!user) {
    window.location.href = 'login.html';
    return null;
  }
  if (roles && roles.length && !roles.includes(user.role)) {
    window.location.href = 'index.html';
    return null;
  }
  return user;
}

function logout() {
  setUser(null);
  window.location.href = 'index.html';
}

function badgeClass(status) {
  const s = (status || '').toLowerCase();
  if (s === 'pending') return 'badge-pending';
  if (s === 'confirmed') return 'badge-confirmed';
  if (s === 'completed') return 'badge-completed';
  if (s === 'cancelled') return 'badge-cancelled';
  return '';
}

function showAlert(el, message, type) {
  if (!el) return;
  el.className = 'alert alert-' + (type || 'info');
  el.textContent = message;
  el.classList.remove('hidden');
}

function formatMoney(n) {
  return '₹' + Number(n).toFixed(2);
}
