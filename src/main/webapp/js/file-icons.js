/**
 * Material Icon 스타일 파일 아이콘 시스템
 */

// 파일 타입별 아이콘 SVG 경로 매핑
const fileIcons = {
    // 폴더
    folder: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M2 3C2 2.44772 2.44772 2 3 2H6.58579C6.851 2 7.10536 2.10536 7.29289 2.29289L8.70711 3.70711C8.89464 3.89464 9.149 4 9.41421 4H13C13.5523 4 14 4.44772 14 5V13C14 13.5523 13.5523 14 13 14H3C2.44772 14 2 13.5523 2 13V3Z" fill="#90A4AE" stroke="#607D8B" stroke-width="0.5"/>
    </svg>`,
    folderOpen: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M2 3C2 2.44772 2.44772 2 3 2H6.58579C6.851 2 7.10536 2.10536 7.29289 2.29289L8.70711 3.70711C8.89464 3.89464 9.149 4 9.41421 4H13C13.5523 4 14 4.44772 14 5V13C14 13.5523 13.5523 14 13 14H3C2.44772 14 2 13.5523 2 13V3Z" fill="#90A4AE" stroke="#607D8B" stroke-width="0.5"/>
        <path d="M2 5L4 7L2 9V5Z" fill="#B0BEC5"/>
    </svg>`,
    
    // 텍스트 파일
    txt: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#90A4AE" stroke="#607D8B" stroke-width="0.5"/>
        <line x1="5" y1="5" x2="11" y2="5" stroke="#607D8B" stroke-width="1"/>
        <line x1="5" y1="7" x2="11" y2="7" stroke="#607D8B" stroke-width="1"/>
        <line x1="5" y1="9" x2="9" y2="9" stroke="#607D8B" stroke-width="1"/>
    </svg>`,
    
    // Markdown
    md: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#42A5F5" stroke="#1976D2" stroke-width="0.5"/>
        <path d="M5 5L7 7L9 5M7 7V11" stroke="#FFFFFF" stroke-width="1" stroke-linecap="round"/>
        <line x1="11" y1="5" x2="11" y2="11" stroke="#FFFFFF" stroke-width="1"/>
    </svg>`,
    
    // PDF
    pdf: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#F44336" stroke="#C62828" stroke-width="0.5"/>
        <path d="M5 5H11M5 7H11M5 9H9" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // JSON
    json: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#FFA726" stroke="#E65100" stroke-width="0.5"/>
        <path d="M6 5L5 7L6 9M10 5L11 7L10 9M7 11L9 5" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // Java
    java: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#F57C00" stroke="#E65100" stroke-width="0.5"/>
        <path d="M6 5C6 5 7 6 8 5C8 5 9 6 8 7C8 7 7 8 6 7C6 7 5 6 6 5Z" fill="#FFFFFF"/>
        <path d="M10 7C10 7 11 8 10 9C10 9 9 10 8 9C8 9 7 8 8 7C8 7 9 6 10 7Z" fill="#FFFFFF"/>
    </svg>`,
    
    // JavaScript
    js: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#FFD600" stroke="#F57F17" stroke-width="0.5"/>
        <path d="M6 5L8 7L6 9M10 5L10 7L8 7L10 9" stroke="#000000" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // CSS
    css: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#42A5F5" stroke="#1976D2" stroke-width="0.5"/>
        <path d="M5 5L7 7L5 9M11 5L9 7L11 9" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // HTML
    html: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#FF6D00" stroke="#E65100" stroke-width="0.5"/>
        <path d="M5 5L7 7L5 9M11 5L9 7L11 9M7 5V9" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // JSP
    jsp: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#9C27B0" stroke="#6A1B9A" stroke-width="0.5"/>
        <path d="M5 5L7 7L5 9M11 5L9 7L11 9" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // XML
    xml: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#66BB6A" stroke="#2E7D32" stroke-width="0.5"/>
        <path d="M5 5L7 7L5 9M11 5L9 7L11 9M7 5V9" stroke="#FFFFFF" stroke-width="0.8" stroke-linecap="round"/>
    </svg>`,
    
    // 기본 파일
    default: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="3" y="2" width="10" height="12" rx="1" fill="#90A4AE" stroke="#607D8B" stroke-width="0.5"/>
        <line x1="5" y1="5" x2="11" y2="5" stroke="#607D8B" stroke-width="0.8"/>
        <line x1="5" y1="8" x2="11" y2="8" stroke="#607D8B" stroke-width="0.8"/>
        <line x1="5" y1="11" x2="9" y2="11" stroke="#607D8B" stroke-width="0.8"/>
    </svg>`
};

/**
 * 파일명으로 아이콘 가져오기
 */
window.getFileIcon = function(filename) {
    if (!filename) return fileIcons.default;
    
    const lower = filename.toLowerCase();
    
    // 폴더 체크 (향후 확장)
    if (lower.endsWith('/') || filename.includes('📁')) {
        return fileIcons.folder;
    }
    
    // 확장자별 아이콘
    if (lower.endsWith('.txt')) return fileIcons.txt;
    if (lower.endsWith('.md') || lower.endsWith('.markdown')) return fileIcons.md;
    if (lower.endsWith('.pdf')) return fileIcons.pdf;
    if (lower.endsWith('.json')) return fileIcons.json;
    if (lower.endsWith('.java')) return fileIcons.java;
    if (lower.endsWith('.js')) return fileIcons.js;
    if (lower.endsWith('.css')) return fileIcons.css;
    if (lower.endsWith('.html') || lower.endsWith('.htm')) return fileIcons.html;
    if (lower.endsWith('.jsp')) return fileIcons.jsp;
    if (lower.endsWith('.xml')) return fileIcons.xml;
    
    return window.fileIcons.default;
};

/**
 * 폴더 아이콘 가져오기
 */
window.getFolderIcon = function(isOpen = false) {
    return isOpen ? window.fileIcons.folderOpen : window.fileIcons.folder;
};

