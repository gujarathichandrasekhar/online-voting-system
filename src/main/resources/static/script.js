/* ===== READ FORM ID ===== */
const params = new URLSearchParams(window.location.search);
const formId = params.get("id") || 1;

let formData = null;

/* ===== LOAD FORM ===== */
fetch("/forms/getbyid?id=" + formId)
  .then(res => res.json())
  .then(data => {
    formData = data;

    document.getElementById("title").innerText = data.name;
    document.getElementById("desc").innerText = data.description;

    const form = document.getElementById("form");
    form.innerHTML = "";

    data.questions.forEach(q => {
      const div = document.createElement("div");
      div.className = "question";

      const label = document.createElement("label");
      label.innerHTML =
        q.label + (q.required ? ' <span class="required">*</span>' : '');
      div.appendChild(label);

      /* TEXT */
      if (q.type === "TEXT") {
        const input = document.createElement("input");
        input.type = "text";
        input.dataset.qid = q.id;
        div.appendChild(input);
      }

      /* RADIO (MCQ) */
      if (q.type === "MCQ") {
        q.options.forEach(o => {
          const row = document.createElement("div");

          const r = document.createElement("input");
          r.type = "radio";
          r.name = "q" + q.id;
          r.value = o.value;
          r.dataset.qid = q.id;

          row.appendChild(r);
          row.append(" " + o.value);
          div.appendChild(row);
        });

        /* ✅ ADD CHART BUTTONS (ONLY FOR MCQ) */
        const chartDiv = document.createElement("div");
        chartDiv.style.marginTop = "8px";
        chartDiv.innerHTML = `
          <button type="button" onclick="viewBarChart(${q.id})">Bar Chart</button>
          <button type="button" onclick="viewPieChart(${q.id})">Pie Chart</button>
        `;
        div.appendChild(chartDiv);
      }

      /* DROPDOWN */
      if (q.type === "DROPDOWN") {
        const select = document.createElement("select");
        select.dataset.qid = q.id;

        const def = document.createElement("option");
        def.value = "";
        def.text = "-- Select --";
        select.appendChild(def);

        q.options.forEach(o => {
          const opt = document.createElement("option");
          opt.value = o.value;
          opt.text = o.value;
          select.appendChild(opt);
        });

        div.appendChild(select);
      }

      /* CHECKBOX */
      if (q.type === "CHECKBOX") {
        q.options.forEach(o => {
          const row = document.createElement("div");

          const cb = document.createElement("input");
          cb.type = "checkbox";
          cb.value = o.value;
          cb.dataset.qid = q.id;

          row.appendChild(cb);
          row.append(" " + o.value);
          div.appendChild(row);
        });
      }

      /* FILE */
      if (q.type === "FILE") {
        const input = document.createElement("input");
        input.type = "file";
        input.dataset.qid = q.id;
        div.appendChild(input);
      }

      form.appendChild(div);
    });
  });

/* ===== SUBMIT FORM (UNCHANGED) ===== */
async function submitForm() {
  let map = {};
  let missing = [];

  for (const el of document.querySelectorAll("input, select")) {
    const qid = el.dataset.qid;
    if (!qid) continue;

    if ((el.type === "text" || el.tagName === "SELECT") && el.value)
      map[qid] = el.value;

    if (el.type === "radio" && el.checked)
      map[qid] = el.value;

    if (el.type === "checkbox" && el.checked) {
      if (!map[qid]) map[qid] = [];
      map[qid].push(el.value);
    }

    if (el.type === "file" && el.files.length > 0) {
      const fd = new FormData();
      fd.append("file", el.files[0]);
      const res = await fetch("/upload", { method: "POST", body: fd });
      map[qid] = await res.text();
    }
  }

  formData.questions.forEach(q => {
    if (q.required && !map[q.id]) missing.push(q.label);
  });

  if (missing.length) {
    alert("Please fill required fields:\n" + missing.join(", "));
    return;
  }

  const answers = [];
  for (const qid in map) {
    let val = map[qid];
    if (Array.isArray(val)) val = val.join(", ");
    answers.push({ question: { id: qid }, value: val });
  }

  fetch("/responses", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ form: { id: formData.id }, answers })
  })
  .then(r => r.text())
  .then(msg => alert(msg + " ✅"))
  .catch(() => alert("Submit failed ❌"));
}

/* ===== NEW REPORT FUNCTIONS (SAFE ADD) ===== */
function downloadPdf() {
  window.open(`/reports/pdf?formId=${formId}`);
}

function viewBarChart(qid) {
  window.open(`/reports/barchart?formId=${formId}&questionId=${qid}`);
}

function viewPieChart(qid) {
  window.open(`/reports/piechart?formId=${formId}&questionId=${qid}`);
}
