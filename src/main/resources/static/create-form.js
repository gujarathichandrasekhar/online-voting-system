let questions = [];

/* ================= ADD QUESTION ================= */
function addQuestion() {

  const label = document.getElementById("qLabel").value.trim();
  const type = document.getElementById("qType").value;
  const required = document.getElementById("qRequired").checked;
  const opts = document.getElementById("qOptions").value.trim();

  /* 🔴 BASIC VALIDATION */
  if (!label) {
    alert("Question label is required");
    return;
  }

  /* 🔴 OPTION VALIDATION FOR MCQ / DROPDOWN / CHECKBOX */
  if (
    (type === "MCQ" || type === "DROPDOWN" || type === "CHECKBOX")
    && (!opts || !opts.includes(","))
  ) {
    alert(
      "Please enter MULTIPLE options separated by commas.\n\nExample:\nOption1, Option2, Option3"
    );
    return;
  }

  /* BUILD QUESTION OBJECT */
  let question = {
    label: label,
    type: type,
    required: required
  };

  /* ADD OPTIONS IF REQUIRED */
  if (opts && type !== "TEXT" && type !== "FILE") {
    question.options = opts.split(",").map(o => ({
      value: o.trim()
    }));
  }

  /* PUSH TO QUESTIONS ARRAY */
  questions.push(question);

  /* SHOW QUESTION IN UI */
  document.getElementById("questions").innerHTML +=
    `<div class="q-box">
        <b>${label}</b> (${type})
     </div>`;

  /* CLEAR INPUTS */
  document.getElementById("qLabel").value = "";
  document.getElementById("qOptions").value = "";
  document.getElementById("qRequired").checked = false;
}

/* ================= CREATE FORM ================= */
function submitForm() {

  const formName = document.getElementById("formName").value.trim();
  const formDesc = document.getElementById("formDesc").value.trim();

  /* 🔴 FORM VALIDATION */
  if (!formName) {
    alert("Form name is required");
    return;
  }

  if (questions.length === 0) {
    alert("Please add at least one question");
    return;
  }

  const payload = {
    name: formName,
    description: formDesc,
    questions: questions
  };

  /* 🔥 BACKEND CALL */
  fetch("/forms", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
  .then(res => {
    if (!res.ok) throw new Error("Failed to create form");
    return res.json();
  })
  .then(data => {
    alert("Form created successfully!\nForm ID: " + data.id);

    /* 🔥 AUTO OPEN CREATED FORM */
    window.location.href = "index.html?id=" + data.id;
  })
  .catch(err => {
    alert("Error creating form");
    console.error(err);
  });
}
