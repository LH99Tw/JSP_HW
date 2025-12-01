/**
 * 드래그 앤 드롭 파일 업로드
 */

let uploadInProgress = false;
let uploadQueue = [];

/**
 * 드래그 앤 드롭 초기화
 */
function initDragAndDrop() {
    const sidebar = document.querySelector('.vscode-sidebar');
    if (!sidebar) return;
    
    // 드래그 오버 이벤트
    sidebar.addEventListener('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
        if (!uploadInProgress) {
            sidebar.classList.add('drag-over');
        }
    });
    
    sidebar.addEventListener('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        sidebar.classList.remove('drag-over');
    });
    
    sidebar.addEventListener('drop', function(e) {
        e.preventDefault();
        e.stopPropagation();
        sidebar.classList.remove('drag-over');
        
        if (uploadInProgress) {
            return;
        }
        
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            handleFileUpload(Array.from(files));
        }
    });
    
    // 전체 문서 레벨에서도 드래그 방지
    document.addEventListener('dragover', function(e) {
        e.preventDefault();
    });
    
    document.addEventListener('drop', function(e) {
        e.preventDefault();
    });
}

/**
 * 파일 업로드 처리
 */
function handleFileUpload(files) {
    if (uploadInProgress) {
        return;
    }
    
    uploadInProgress = true;
    uploadQueue = files;
    
    // 로딩 오버레이 표시
    showUploadOverlay();
    
    // 파일 업로드 시작
    uploadFilesSequentially(files, 0);
}

/**
 * 파일을 순차적으로 업로드
 */
function uploadFilesSequentially(files, index) {
    if (index >= files.length) {
        // 모든 파일 업로드 완료
        setTimeout(() => {
            hideUploadOverlay();
            uploadInProgress = false;
            uploadQueue = [];
            
            // 파일 목록 새로고침
            if (typeof loadFileList === 'function') {
                loadFileList();
            }
        }, 500);
        return;
    }
    
    const file = files[index];
    const formData = new FormData();
    formData.append('file', file);
    
    // 업로드 진행률 업데이트
    updateUploadProgress(index + 1, files.length, file.name);
    
    // API 엔드포인트 가져오기
    const uploadUrl = window.location.pathname.replace(/\/[^\/]*$/, '') + '/api/upload';
    
    fetch(uploadUrl, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.message || 'Upload failed');
            });
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // 다음 파일 업로드
            uploadFilesSequentially(files, index + 1);
        } else {
            throw new Error(data.message || 'Upload failed');
        }
    })
    .catch(error => {
        console.error('Upload error:', error);
        updateUploadError(file.name, error.message);
        // 에러가 발생해도 다음 파일 계속 업로드
        uploadFilesSequentially(files, index + 1);
    });
}

/**
 * 업로드 오버레이 표시
 */
function showUploadOverlay() {
    let overlay = document.getElementById('upload-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'upload-overlay';
        overlay.innerHTML = `
            <div class="upload-overlay-content">
                <div class="upload-spinner"></div>
                <div class="upload-status">
                    <div class="upload-filename" id="upload-filename">파일 업로드 중...</div>
                    <div class="upload-progress" id="upload-progress">0 / 0</div>
                    <div class="upload-progress-bar">
                        <div class="upload-progress-fill" id="upload-progress-fill"></div>
                    </div>
                </div>
                <div class="upload-errors" id="upload-errors"></div>
            </div>
        `;
        document.body.appendChild(overlay);
    }
    overlay.style.display = 'flex';
    
    // UI 잠금
    document.body.style.pointerEvents = 'none';
    overlay.style.pointerEvents = 'auto';
}

/**
 * 업로드 오버레이 숨기기
 */
function hideUploadOverlay() {
    const overlay = document.getElementById('upload-overlay');
    if (overlay) {
        overlay.style.display = 'none';
    }
    
    // UI 잠금 해제
    document.body.style.pointerEvents = 'auto';
}

/**
 * 업로드 진행률 업데이트
 */
function updateUploadProgress(current, total, filename) {
    const filenameEl = document.getElementById('upload-filename');
    const progressEl = document.getElementById('upload-progress');
    const progressFillEl = document.getElementById('upload-progress-fill');
    
    if (filenameEl) {
        filenameEl.textContent = filename;
    }
    if (progressEl) {
        progressEl.textContent = current + ' / ' + total;
    }
    if (progressFillEl) {
        const percentage = (current / total) * 100;
        progressFillEl.style.width = percentage + '%';
    }
}

/**
 * 업로드 에러 표시
 */
function updateUploadError(filename, error) {
    const errorsEl = document.getElementById('upload-errors');
    if (errorsEl) {
        const errorItem = document.createElement('div');
        errorItem.className = 'upload-error-item';
        errorItem.textContent = filename + ': ' + error;
        errorsEl.appendChild(errorItem);
    }
}

