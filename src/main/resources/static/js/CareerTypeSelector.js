// ✅ 이 파일 내부에서 React를 직접 import
import React, { useState } from "https://cdn.jsdelivr.net/npm/react@18.2.0/+esm";
import { createRoot } from "https://cdn.jsdelivr.net/npm/react-dom@18.2.0/client/+esm";

function CareerTypeSelector() {
    const [careerType, setCareerType] = useState('');

    const handleChange = (e) => {
        const value = e.target.value;
        setCareerType(value);
        document.getElementById('filter-career-type').value = value;
    };

    return (
        <div className="career-type-selector">
            <label className="me-3">
                <input type="radio" name="careerType" value="" onChange={handleChange} checked={careerType === ''} />
                전체
            </label>
            <label className="me-3">
                <input type="radio" name="careerType" value="new" onChange={handleChange} checked={careerType === 'new'} />
                신입
            </label>
            <label>
                <input type="radio" name="careerType" value="exp" onChange={handleChange} checked={careerType === 'exp'} />
                경력
            </label>
        </div>
    );
}

const container = document.getElementById('react-career-type-selector');
if (container) {
    const root = createRoot(container);
    root.render(React.createElement(CareerTypeSelector));
} else {
    console.error("❌ react-career-type-selector element가 존재하지 않습니다.");
}
