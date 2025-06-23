// faculty-workload.js
// Handles dynamic filter controls and AJAX table update for faculty workload page

document.addEventListener('DOMContentLoaded', function () {
    const schoolYearSelect = document.getElementById('schoolYear');
    const semesterSelect = document.getElementById('semester');
    const tableBody = document.getElementById('workloadTableBody');
    const semesterInfo = document.getElementById('semesterInfo');

    // LocalStorage keys
    const LS_YEAR = 'facultyWorkloadSchoolYear';
    const LS_SEM = 'facultyWorkloadSemester';

    // Fetch filter options from backend
    function fetchFilters() {
        fetch('/api/faculty/workload-filters')
            .then(res => res.json())
            .then(data => {
                populateFilters(data);
                restoreSelections();
                fetchAndRenderTable();
            });
    }

    // Populate dropdowns
    function populateFilters(data) {
        // School Years (descending order)
        schoolYearSelect.innerHTML = '';
        const sortedYears = [...data.schoolYears].sort((a, b) => b.value.localeCompare(a.value));
        sortedYears.forEach((yr) => {
            const opt = document.createElement('option');
            opt.value = yr.value;
            opt.textContent = yr.label;
            schoolYearSelect.appendChild(opt);
        });
        // Semesters (no Summer)
        semesterSelect.innerHTML = '';
        data.semesters.forEach(sem => {
            if (sem.value !== 'Summer') {
                const opt = document.createElement('option');
                opt.value = sem.value;
                opt.textContent = sem.label;
                semesterSelect.appendChild(opt);
            }
        });
        // Auto-select latest year if no localStorage
        if (!localStorage.getItem(LS_YEAR) && sortedYears.length > 0) {
            schoolYearSelect.selectedIndex = 0;
        }
        // Auto-select first semester if no localStorage
        if (!localStorage.getItem(LS_SEM) && semesterSelect.options.length > 0) {
            semesterSelect.selectedIndex = 0;
        }
    }

    // Restore last selections from localStorage
    function restoreSelections() {
        const lastYear = localStorage.getItem(LS_YEAR);
        const lastSem = localStorage.getItem(LS_SEM);
        if (lastYear) schoolYearSelect.value = lastYear;
        if (lastSem) semesterSelect.value = lastSem;
    }

    // Save selections to localStorage
    function saveSelections() {
        localStorage.setItem(LS_YEAR, schoolYearSelect.value);
        localStorage.setItem(LS_SEM, semesterSelect.value);
    }

    // Fetch and render table
    function fetchAndRenderTable() {
        saveSelections();
        const year = schoolYearSelect.value;
        const sem = semesterSelect.value;
        fetch(`/api/faculty/workload-data?schoolYear=${encodeURIComponent(year)}&semester=${encodeURIComponent(sem)}`)
            .then(res => res.json())
            .then(data => {
                renderTable(data);
                updateSemesterInfo(year, sem);
            });
    }

    // Render table rows
    function renderTable(data) {
        tableBody.innerHTML = '';
        if (!data || data.length === 0) {
            const tr = document.createElement('tr');
            const td = document.createElement('td');
            td.colSpan = 8;
            td.style.textAlign = 'center';
            td.textContent = 'No workload assigned.';
            tr.appendChild(td);
            tableBody.appendChild(tr);
            return;
        }
        data.forEach((row, idx) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${row.no}</td>
                <td>${row.section}</td>
                <td>${row.subjectCode}</td>
                <td>${row.description}</td>
                <td>${row.schedule}</td>
                <td>${row.room}</td>
                <td>${row.units}</td>
                <td>
                    <div class="action-buttons">
                        <a href="/faculty-class-list?section=${encodeURIComponent(row.sectionParam)}&subject=${encodeURIComponent(row.subjectParam)}" class="view-button-small">Class List</a>
                        <a href="/faculty-grading-sheet?section=${encodeURIComponent(row.sectionParam)}&course=${encodeURIComponent(row.subjectParam)}" class="view-button-small">Grading</a>
                    </div>
                </td>
            `;
            tableBody.appendChild(tr);
        });
    }

    // Update semester info text
    function updateSemesterInfo(year, sem) {
        // Find label for year and sem
        const yearLabel = schoolYearSelect.options[schoolYearSelect.selectedIndex]?.textContent || year;
        const semLabel = semesterSelect.options[semesterSelect.selectedIndex]?.textContent || sem;
        semesterInfo.textContent = `${yearLabel} / ${semLabel.toUpperCase()} SEMESTER`;
    }

    // Event listeners
    schoolYearSelect.addEventListener('change', fetchAndRenderTable);
    semesterSelect.addEventListener('change', fetchAndRenderTable);

    // Init
    fetchFilters();
});
