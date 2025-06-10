import streamlit as st
import fitz
import os
from langchain.docstore.document import Document
from langchain.embeddings import OpenAIEmbeddings
from langchain.vectorstores import FAISS
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain
from langchain_community.llms import Ollama
from langchain.llms import OpenAI
from sklearn.metrics.pairwise import cosine_similarity

st.set_page_config(page_title="Job Application Assistant", layout="wide")
st.title("💼 Job Application Assistant")

os.environ["OPENAI_API_KEY"] = "secret"

st.sidebar.header("📄 Upload or Paste Your CV")
uploaded_pdf = st.sidebar.file_uploader("Upload your CV as PDF", type="pdf")
cv_text_input = st.sidebar.text_area("Or paste your CV here", height=150)

st.sidebar.header("🧾 Job Description")
job_desc_input = st.sidebar.text_area("Paste the job description here", height=150)

st.sidebar.header("⚙️ Model Selection")
model_option = st.sidebar.selectbox("Choose a model", ["mistral (local)", "gpt-3.5-turbo (OpenAI)"])

run_clicked = st.sidebar.button("🚀 Run Assistant")

def extract_text_from_pdf(file) -> str:
    with open("temp_cv.pdf", "wb") as f:
        f.write(file.read())
    doc = fitz.open("temp_cv.pdf")
    return "".join([page.get_text() for page in doc]).strip()

cv_text = ""
if uploaded_pdf:
    cv_text = extract_text_from_pdf(uploaded_pdf)
elif cv_text_input:
    cv_text = cv_text_input

if cv_text:
    st.session_state.cv_text = cv_text
if job_desc_input:
    st.session_state.job_desc = job_desc_input

if run_clicked and "cv_text" in st.session_state and "job_desc" in st.session_state:
    st.subheader("📄 CV Preview")
    st.text_area("Extracted CV Text", value=st.session_state.cv_text, height=200)

    st.subheader("📌 Job Description Preview")
    st.text_area("Job Description", value=st.session_state.job_desc, height=150)

    documents = [
        Document(page_content=st.session_state.cv_text),
        Document(page_content=st.session_state.job_desc)
    ]
    embedding_model = OpenAIEmbeddings()
    vector_store = FAISS.from_documents(documents, embedding_model)

    query = "data analysis and dashboards"
    similar_docs = vector_store.similarity_search(query)

    cv_embedding = embedding_model.embed_query(st.session_state.cv_text)
    job_embedding = embedding_model.embed_query(st.session_state.job_desc)
    similarity_score = cosine_similarity([cv_embedding], [job_embedding])[0][0]
    similarity_percent = round(similarity_score * 100, 2)

    st.subheader("🔍 Semantic Similarity Score")
    st.metric(label="CV ↔ Job Match", value=f"{similarity_percent}%")

    with st.expander("🔗 Most Relevant Match (Vector Search)", expanded=False):
        st.write(similar_docs[0].page_content)

    template = """
    You are a career assistant.

    Here is the job description:
    {job_desc}

    Here is the candidate's CV:
    {cv_text}

    1. Suggest 3 ways the candidate can improve the CV to better match the job.
    2. Write a short and professional motivation letter tailored to the job.
    """
    prompt = PromptTemplate(input_variables=["job_desc", "cv_text"], template=template)

    def generate_output(llm):
        chain = LLMChain(llm=llm, prompt=prompt)
        return chain.run({
            "job_desc": st.session_state.job_desc,
            "cv_text": st.session_state.cv_text
        })

    st.subheader("🧠 AI Suggestions & Motivation Letter")
    if model_option == "mistral (local)":
        mistral_llm = Ollama(model="mistral")
        result = generate_output(mistral_llm)
        st.markdown("### 🤖 Mistral Output")
        st.markdown(result)
    elif model_option == "gpt-3.5-turbo (OpenAI)":
        gpt_llm = OpenAI(max_tokens=1000)
        result = generate_output(gpt_llm)
        st.markdown("### ☁️ OpenAI GPT Output")
        st.markdown(result)

elif run_clicked:
    st.warning("⚠️ Please provide both a CV (upload or paste) and a job description before running the assistant.")
