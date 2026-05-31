//  CSRF helpers (Spring Security) 
const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

//  Toast system 
function showToast(message, type = 'info', duration = 3500) {
  const icons = {
    success: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>',
    danger:  '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>',
    warning: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>',
    info:    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>',
  };
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `${icons[type] || ''}<span>${message}</span><span class="toast-close" onclick="this.parentElement.remove()">✕</span>`;
  container.appendChild(toast);
  setTimeout(() => { toast.style.animation = 'toastIn .25s ease reverse'; setTimeout(() => toast.remove(), 250); }, duration);
}

//  Confirm dialog 
function confirmAction(title, message, onConfirm, type = 'danger') {
  const overlay = document.getElementById('confirm-overlay');
  if (!overlay) return;
  overlay.querySelector('.confirm-title').textContent = title;
  overlay.querySelector('.confirm-msg').textContent   = message;
  overlay.querySelector('.confirm-icon').className    = `confirm-icon ${type}`;
  const confirmBtn = overlay.querySelector('#confirm-ok');
  const newBtn = confirmBtn.cloneNode(true);
  confirmBtn.parentNode.replaceChild(newBtn, confirmBtn);
  newBtn.addEventListener('click', () => { overlay.classList.remove('open'); onConfirm(); });
  overlay.classList.add('open');
}
document.getElementById('confirm-cancel')?.addEventListener('click', () => {
  document.getElementById('confirm-overlay')?.classList.remove('open');
});

//  Modal helpers 
function openModal(id)  { document.getElementById(id)?.classList.add('open'); }
function closeModal(id) { document.getElementById(id)?.classList.remove('open'); }
document.querySelectorAll('.modal-overlay').forEach(m => {
  m.addEventListener('click', e => { if (e.target === m) m.classList.remove('open'); });
});
document.querySelectorAll('.modal-close').forEach(btn => {
  btn.addEventListener('click', () => btn.closest('.modal-overlay')?.classList.remove('open'));
});

//  Live search for tables 
function liveSearch(inputId, tableId, cols) {
  const input = document.getElementById(inputId);
  const table = document.getElementById(tableId);
  if (!input || !table) return;
  input.addEventListener('input', () => {
    const q = input.value.toLowerCase();
    const rows = table.querySelectorAll('tbody tr');
    let visible = 0;
    rows.forEach(row => {
      const cells = row.querySelectorAll('td');
      const text = (cols ? cols.map(i => cells[i]?.textContent).join(' ') : row.textContent).toLowerCase();
      const show = q === '' || text.includes(q);
      row.style.display = show ? '' : 'none';
      if (show) visible++;
    });
    const counter = document.getElementById(inputId + '-count');
    if (counter) counter.textContent = `Showing ${visible} of ${rows.length}`;
  });
}

//  Live filter (select) 
function liveFilter(selectId, tableId, col) {
  const sel   = document.getElementById(selectId);
  const table = document.getElementById(tableId);
  if (!sel || !table) return;
  sel.addEventListener('change', () => {
    const val = sel.value.toLowerCase();
    table.querySelectorAll('tbody tr').forEach(row => {
      const cell = row.querySelectorAll('td')[col];
      const match = val === '' || cell?.textContent.toLowerCase().includes(val);
      row.style.display = match ? '' : 'none';
    });
  });
}

//  Topbar date 
const dateEl = document.getElementById('topbar-date');
if (dateEl) {
  dateEl.textContent = new Date().toLocaleDateString('en-US', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
  });
}

//  Delete confirmation on .btn-delete 
document.querySelectorAll('.btn-delete').forEach(btn => {
  btn.addEventListener('click', e => {
    e.preventDefault();
    const href = btn.getAttribute('href') || btn.dataset.href;
    const name = btn.dataset.name || 'this item';
    confirmAction(
      'Confirm Deletion',
      `Are you sure you want to delete ${name}? This action cannot be undone.`,
      () => window.location.href = href,
      'danger'
    );
  });
});

//  Check-in/Check-out confirmation 
document.querySelectorAll('.btn-checkin').forEach(btn => {
  btn.addEventListener('click', e => {
    e.preventDefault();
    const href = btn.dataset.href;
    confirmAction('Confirm Check-In', 'Proceed with check-in for this booking?',
      () => window.location.href = href, 'warning');
  });
});
document.querySelectorAll('.btn-checkout').forEach(btn => {
  btn.addEventListener('click', e => {
    e.preventDefault();
    const href = btn.dataset.href;
    confirmAction('Confirm Check-Out', 'Proceed with check-out? A bill will be generated.',
      () => window.location.href = href, 'warning');
  });
});

//  Auto-dismiss flash alerts 
document.querySelectorAll('.alert[data-auto-dismiss]').forEach(el => {
  setTimeout(() => el.remove(), 4000);
});

//  Occupancy cells tooltip ─
document.querySelectorAll('.occ-cell[title]').forEach(cell => {
  cell.setAttribute('aria-label', cell.title);
});

//  Bill amount preview 
const roomChargeInput  = document.getElementById('roomCharges');
const foodChargeInput  = document.getElementById('foodCharges');
const totalPreview     = document.getElementById('totalPreview');
if (roomChargeInput && foodChargeInput && totalPreview) {
  function updateTotal() {
    const r = parseFloat(roomChargeInput.value) || 0;
    const f = parseFloat(foodChargeInput.value) || 0;
    totalPreview.textContent = '$' + (r + f).toLocaleString('en-US', { minimumFractionDigits: 2 });
  }
  roomChargeInput.addEventListener('input', updateTotal);
  foodChargeInput.addEventListener('input', updateTotal);
  updateTotal();
}

//  Toggle room view (grid ↔ table) ─
const viewToggle = document.getElementById('view-toggle');
const roomGrid   = document.getElementById('room-grid-view');
const roomTable  = document.getElementById('room-table-view');
if (viewToggle && roomGrid && roomTable) {
  viewToggle.addEventListener('click', () => {
    const isGrid = roomGrid.style.display !== 'none';
    roomGrid.style.display  = isGrid ? 'none' : '';
    roomTable.style.display = isGrid ? '' : 'none';
    viewToggle.textContent  = isGrid ? '⊞ Grid View' : '≡ List View';
  });
}

//  Inventory low-stock highlight 
document.querySelectorAll('[data-qty][data-threshold]').forEach(el => {
  const qty = parseInt(el.dataset.qty);
  const thr = parseInt(el.dataset.threshold);
  const fill = el.querySelector('.stock-fill');
  if (fill) {
    const pct = Math.min(100, (qty / (thr * 2)) * 100);
    fill.style.width = pct + '%';
    if (qty <= thr) fill.classList.add('low');
    else if (qty <= thr * 1.5) fill.classList.add('warn');
  }
});
