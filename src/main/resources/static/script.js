const BASE = location.origin;

const qs  = (sel) => document.querySelector(sel);
const fmt = (n) => (n ?? 0).toLocaleString("en-IN");

async function fetchJSON(path, options = {}) {
  const res = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/* ---------------- PRODUCTS ---------------- */

async function loadProducts() {
  const data = await fetchJSON(`${BASE}/api/products`);

  qs("#products-grid").innerHTML = data.map(p => `
    <div class="card">
      <h4>${p.productName}</h4>
      <div>₹ ${fmt(p.price)}</div>
      <div>Stock: ${p.quantity}</div>
    </div>
  `).join("");

  const sel = qs("#o-product");
  if (sel) {
    sel.innerHTML = data.map(p =>
      `<option value="${p.productId}">${p.productName}</option>`
    ).join("");
  }
}

async function addProduct() {
  const body = {
    productName: qs("#p-name").value,
    category: qs("#p-cat").value,
    price: Number(qs("#p-price").value),
    quantity: Number(qs("#p-qty").value)
  };

  await fetchJSON(`${BASE}/api/products`, {
    method: "POST",
    body: JSON.stringify(body)
  });

  loadProducts();
}

/* ---------------- CUSTOMERS ---------------- */

async function loadCustomers() {
  const data = await fetchJSON(`${BASE}/api/customers`);

  qs("#customers-body").innerHTML = data.map(c => `
    <tr>
      <td>${c.customerId}</td>
      <td>${c.customerName}</td>
      <td>${c.email || ""}</td>
      <td>${c.phone || ""}</td>
    </tr>
  `).join("");

  const sel = qs("#o-customer");
  if (sel) {
    sel.innerHTML = data.map(c =>
      `<option value="${c.customerId}">${c.customerName}</option>`
    ).join("");
  }
}

async function addCustomer() {
  const body = {
    customerName: qs("#c-name").value,
    email: qs("#c-email").value,
    phone: qs("#c-phone").value,
    address: qs("#c-addr").value
  };

  await fetchJSON(`${BASE}/api/customers`, {
    method: "POST",
    body: JSON.stringify(body)
  });

  loadCustomers();
}

/* ---------------- SUPPLIERS ---------------- */

async function loadSuppliers() {
  const data = await fetchJSON(`${BASE}/api/suppliers`);

  qs("#suppliers-body").innerHTML = data.map(s => `
    <tr>
      <td>${s.supplierId}</td>
      <td>${s.name}</td>
      <td>${s.email || ""}</td>
      <td>${s.phone || ""}</td>
    </tr>
  `).join("");
}

async function addSupplier() {
  const body = {
    name: qs("#s-name").value,
    email: qs("#s-email").value,
    phone: qs("#s-phone").value
  };

  if (!body.name) {
    alert("Enter supplier name");
    return;
  }

  await fetchJSON(`${BASE}/api/suppliers`, {
    method: "POST",
    body: JSON.stringify(body)
  });

  qs("#s-name").value = "";
  qs("#s-email").value = "";
  qs("#s-phone").value = "";

  loadSuppliers();
}

/* ---------------- ORDERS ---------------- */

async function loadOrders() {
  const data = await fetchJSON(`${BASE}/api/orders`);

  qs("#orders-body").innerHTML = data.map(o => `
    <tr>
      <td>${o.orderId}</td>
      <td>${o.customerName || ""}</td>
      <td>${o.status}</td>
      <td>${fmt(o.totalAmount)}</td>
    </tr>
  `).join("");
}

async function createOrder() {
  const body = {
    customerId: Number(qs("#o-customer").value),
    items: [{
      productId: Number(qs("#o-product").value),
      quantity: Number(qs("#o-qty").value)
    }]
  };

  await fetchJSON(`${BASE}/api/orders`, {
    method: "POST",
    body: JSON.stringify(body)
  });

  loadOrders();
}

/* ---------------- INIT ---------------- */

window.onload = function () {

  qs("#add-product").onclick = addProduct;
  qs("#refresh-products").onclick = loadProducts;

  qs("#add-customer").onclick = addCustomer;
  qs("#refresh-customers").onclick = loadCustomers;

  qs("#add-supplier").onclick = addSupplier;
  qs("#refresh-suppliers").onclick = loadSuppliers;

  qs("#create-order").onclick = createOrder;
  qs("#refresh-orders").onclick = loadOrders;

  loadProducts();
  loadCustomers();
  loadSuppliers();
  loadOrders();
};