from fastapi import FastAPI, UploadFile, Form
from fastapi.responses import JSONResponse
from model import get_resume_match_score
from pdf_parser import extract_text_from_pdf
import os
import shutil

app = FastAPI()

UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)

@app.post("/match/")
async def match_resume_to_jd(file: UploadFile, jd_text: str = Form(...)):
    try:
        file_path = os.path.join(UPLOAD_DIR, file.filename)
        with open(file_path, "wb") as f:
            shutil.copyfileobj(file.file, f)

        resume_text = extract_text_from_pdf(file_path)

        result = get_resume_match_score(resume_text, jd_text)
        return JSONResponse(content=result)
    except Exception as e:
        return JSONResponse(content={
            "match_score": 0,
            "justification": f"API Error: {e}"
        }, status_code=500)
