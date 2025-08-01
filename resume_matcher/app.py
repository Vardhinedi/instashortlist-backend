from fastapi import FastAPI, UploadFile, Form
from fastapi.responses import JSONResponse
from resume_matcher.utils import extract_text_from_pdf
from resume_matcher.model import calculate_match_score

app = FastAPI()

@app.post("/match")
async def match_resume(
    resume: UploadFile,
    job_description: str = Form(...)
):
    try:
        resume_text = await extract_text_from_pdf(resume)
        score, reason = calculate_match_score(resume_text, job_description)
        return {"score": score, "reason": reason}
    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)
