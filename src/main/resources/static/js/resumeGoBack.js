function goBackToTab() {
    const urlParams = new URLSearchParams(window.location.search);
    const ref = urlParams.get('ref') || 'back';

    if (ref === 'tab') {
        const tab = urlParams.get('fromTab') || '1';
        location.href = `/resumes?tab=${tab}`;
    } else {
        history.back();
    }
}
