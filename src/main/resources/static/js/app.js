// TRPG Damage Calculator - Frontend JavaScript

const API_BASE = '/api';
let currentJob = null;

// Show job category (normal/hidden)
function showJobCategory(category, event) {
    document.querySelectorAll('.job-category').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    
    document.getElementById(category + '-jobs').style.display = 'block';
    if (event && event.target) {
        event.target.classList.add('active');
    }
}

// Select a job
function selectJob(job, event) {
    currentJob = job;
    
    // Update visual selection
    document.querySelectorAll('.job-card').forEach(card => card.classList.remove('selected'));
    if (event && event.target) {
        var card = event.target.closest('.job-card');
        if (card) {
            card.classList.add('selected');
        }
    }
    
    // Show skill section
    document.getElementById('skill-section').style.display = 'block';
    
    // Hide all skill forms
    document.querySelectorAll('.skill-form').forEach(form => form.style.display = 'none');
    
    // Show selected job's skills
    const skillForm = document.getElementById(job + '-skills');
    if (skillForm) {
        skillForm.style.display = 'block';
    }
    
    // Update title
    const jobNames = {
        'warrior': '⚔️ 전사',
        'archer': '🏹 궁수',
        'rogue': '🗡️ 도적',
        'mage': '🔮 마법사',
        'priest': '✝️ 사제',
        'samurai': '⚔️ 무사',
        'berserker': '💢 버서커',
        'gambler': '🎰 겜블러'
    };
    document.getElementById('selected-job-title').textContent = jobNames[job] + ' 기술';
    
    // Scroll to skill section
    document.getElementById('skill-section').scrollIntoView({ behavior: 'smooth' });
}

// Roll dice
async function rollDice() {
    const dices = parseInt(document.getElementById('diceCount').value) || 1;
    const sides = parseInt(document.getElementById('diceSides').value) || 6;
    
    try {
        const response = await fetch(`${API_BASE}/dice`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ dices, sides })
        });
        
        const data = await response.json();
        
        document.getElementById('diceResult').innerHTML = `
            <div class="damage-label">${dices}D${sides} 결과</div>
            <div class="damage-value">${data.result}</div>
        `;
        
        addLog(`🎲 ${dices}D${sides} 주사위 결과: ${data.result}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Warrior calculations
async function calculateWarrior(skill) {
    const power = parseInt(document.getElementById('warrior-power').value) || 10;
    const maxHealth = parseInt(document.getElementById('warrior-maxHealth').value) || 100;
    const curHealth = parseInt(document.getElementById('warrior-curHealth').value) || 100;
    
    try {
        const response = await fetch(`${API_BASE}/warrior/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ power, maxHealth, curHealth })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '전사 - ' + getSkillName('warrior', skill));
        addLog(`⚔️ 전사 - ${getSkillName('warrior', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

async function calculateWarriorShield() {
    const damageTaken = parseInt(document.getElementById('warrior-damageTaken').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/warrior/shield`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ damageTaken })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '전사 - 육참골단 (반격 데미지)');
        addLog(`⚔️ 전사 - 육참골단`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Archer calculations
async function calculateArcher(skill) {
    const stat = parseInt(document.getElementById('archer-stat').value) || 10;
    const strength = parseInt(document.getElementById('archer-strength').value) || 10;
    const dexterity = parseInt(document.getElementById('archer-dexterity').value) || 10;
    const consecutiveHits = parseInt(document.getElementById('archer-consecutiveHits').value) || 1;
    
    let body;
    let endpoint = skill;
    
    if (skill === 'plain' || skill === 'dash' || skill === 'quickshot') {
        body = { stat };
    } else if (skill === 'plain-dual' || skill === 'dash-dual') {
        body = { strength, dexterity };
    } else if (skill === 'hunt') {
        body = { stat, consecutiveHits };
    }
    
    try {
        const response = await fetch(`${API_BASE}/archer/${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '궁수 - ' + getSkillName('archer', skill));
        addLog(`🏹 궁수 - ${getSkillName('archer', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Rogue calculations
async function calculateRogue(skill) {
    const stat = parseInt(document.getElementById('rogue-stat').value) || 10;
    const dexterity = parseInt(document.getElementById('rogue-dexterity').value) || 10;
    const swiftness = parseInt(document.getElementById('rogue-swiftness').value) || 10;
    const useTwoDice = document.getElementById('rogue-useTwoDice').checked;
    
    let body;
    
    if (skill === 'plain') {
        body = { stat, useTwoDice };
    } else if (skill === 'stab') {
        body = { stat };
    } else if (skill === 'throw') {
        body = { dexterity, swiftness };
    }
    
    try {
        const response = await fetch(`${API_BASE}/rogue/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '도적 - ' + getSkillName('rogue', skill));
        addLog(`🗡️ 도적 - ${getSkillName('rogue', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Mage calculations
async function calculateMage(skill) {
    const intelligence = parseInt(document.getElementById('mage-intelligence').value) || 10;
    const additionalMana = parseInt(document.getElementById('mage-additionalMana').value) || 0;
    const damageTaken = parseInt(document.getElementById('mage-damageTaken').value) || 10;
    const useMana = document.getElementById('mage-useMana').checked;
    
    let body;
    
    if (skill === 'plain') {
        body = { intelligence, useMana };
    } else if (skill === 'magic-bullet') {
        body = { intelligence };
    } else if (skill === 'mana-blast') {
        body = { intelligence, additionalMana };
    } else if (skill === 'magic-guard') {
        body = { damageTaken };
    }
    
    try {
        const response = await fetch(`${API_BASE}/mage/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        
        const data = await response.json();
        const label = skill === 'magic-guard' ? '마법사 - 매직가드 (감소된 데미지)' : '마법사 - ' + getSkillName('mage', skill);
        showDamageResult(data.damage, label);
        addLog(`🔮 마법사 - ${getSkillName('mage', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Priest calculations
async function calculatePriest(skill) {
    const intelligence = parseInt(document.getElementById('priest-intelligence').value) || 10;
    const damageTaken = parseInt(document.getElementById('priest-damageTaken').value) || 10;
    
    let body;
    
    if (skill === 'plain' || skill === 'revenge') {
        body = { intelligence };
    } else if (skill === 'sacrifice') {
        body = { damageTaken };
    }
    
    try {
        const response = await fetch(`${API_BASE}/priest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        
        const data = await response.json();
        const label = skill === 'sacrifice' ? '사제 - 희생 (감소된 데미지)' : '사제 - ' + getSkillName('priest', skill);
        showDamageResult(data.damage, label);
        addLog(`✝️ 사제 - ${getSkillName('priest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Samurai calculations
async function calculateSamurai(skill) {
    const stat = parseInt(document.getElementById('samurai-stat').value) || 10;
    const consumedStamina = parseInt(document.getElementById('samurai-consumedStamina').value) || 0;
    const isMula = document.getElementById('samurai-isMula').checked;
    const kakugo = document.getElementById('samurai-kakugo').checked;
    const seishaKetsudan = document.getElementById('samurai-seishaKetsudan').checked;
    
    let body = { stat, isMula, kakugo, seishaKetsudan };
    
    if (skill === 'final-point') {
        body.consumedStamina = consumedStamina;
    }
    
    try {
        const response = await fetch(`${API_BASE}/samurai/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '무사 - ' + getSkillName('samurai', skill));
        addLog(`⚔️ 무사 - ${getSkillName('samurai', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Berserker calculations
async function calculateBerserker(skill) {
    const stat = parseInt(document.getElementById('berserker-stat').value) || 10;
    const maxHealth = parseInt(document.getElementById('berserker-maxHealth').value) || 100;
    const currentHealth = parseInt(document.getElementById('berserker-currentHealth').value) || 100;
    
    try {
        const response = await fetch(`${API_BASE}/berserker/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, maxHealth, currentHealth })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '버서커 - ' + getSkillName('berserker', skill));
        addLog(`💢 버서커 - ${getSkillName('berserker', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Gambler calculations
async function calculateGambler(skill) {
    const stat = parseInt(document.getElementById('gambler-stat').value) || 10;
    const luck = parseInt(document.getElementById('gambler-luck').value) || 10;
    const reducedLuck = parseInt(document.getElementById('gambler-reducedLuck').value) || 0;
    const jackpotActive = document.getElementById('gambler-jackpotActive').checked;
    
    try {
        const response = await fetch(`${API_BASE}/gambler/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, luck, reducedLuck, jackpotActive })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '겜블러 - ' + getSkillName('gambler', skill));
        addLog(`🎰 겜블러 - ${getSkillName('gambler', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Show damage result
function showDamageResult(damage, label) {
    document.getElementById('damageResult').innerHTML = `
        <div class="damage-label">${label}</div>
        <div class="damage-value">${damage}</div>
    `;
}

// Add log entry
function addLog(title, content = '') {
    const logArea = document.getElementById('calculationLog');
    const timestamp = new Date().toLocaleTimeString('ko-KR');
    
    const logEntry = document.createElement('div');
    logEntry.className = 'log-entry';
    logEntry.innerHTML = `
        <div class="timestamp">[${timestamp}] ${title}</div>
        <div class="log-content">${content}</div>
    `;
    
    logArea.insertBefore(logEntry, logArea.firstChild);
}

// Get skill name in Korean
function getSkillName(job, skill) {
    const skillNames = {
        warrior: {
            'plain': '기본공격',
            'strike': '강타',
            'side': '가로베기',
            'shield': '육참골단'
        },
        archer: {
            'plain': '기본공격 (단일)',
            'plain-dual': '기본공격 (동시)',
            'quickshot': '퀵샷',
            'dash': '대쉬 (단일)',
            'dash-dual': '대쉬 (동시)',
            'hunt': '사냥감'
        },
        rogue: {
            'plain': '기본공격',
            'stab': '쑤시기',
            'throw': '투척/속공'
        },
        mage: {
            'plain': '기본공격',
            'magic-bullet': '마탄',
            'mana-blast': '마나 블래스트',
            'magic-guard': '매직가드'
        },
        priest: {
            'plain': '기본공격',
            'revenge': '복수',
            'sacrifice': '희생'
        },
        samurai: {
            'plain': '기본공격',
            'quick-draw': '발검',
            'battou': '발도',
            'jabeop': '자법',
            'il-seom': '일섬',
            'ranged-attack': '난격',
            'flash-strike': '섬격',
            'final-point': '종점',
            'bloom': '개화'
        },
        berserker: {
            'plain': '기본공격',
            'chop-down': '찍어내리기',
            'smash': '부수기',
            'strike': '일격',
            'mindless-barrage': '무지성 난타',
            'savage-assault': '흉폭한 맹공',
            'last-strike': '최후의 일격'
        },
        gambler: {
            'plain': '기본공격',
            'coin-toss': '코인 토스',
            'joker-card': '조커 카드',
            'blackjack': '블랙잭',
            'yatzy-dice': '야추 다이스',
            'royal-flush': '로얄 플러쉬'
        }
    };
    
    var jobSkills = skillNames[job];
    if (jobSkills && jobSkills[skill]) {
        return jobSkills[skill];
    }
    return skill;
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    addLog('🎮 TRPG 데미지 계산기가 시작되었습니다!');
});
