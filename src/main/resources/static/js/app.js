// TRPG Damage Calculator - Frontend JavaScript

const API_BASE = '/api';
let currentJob = null;

// Show category (defense/essence/normal/hidden/secret)
function showCategory(category, event) {
    document.querySelectorAll('.job-category').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    
    // Handle different category ID patterns
    if (category === 'defense' || category === 'essence') {
        document.getElementById(category + '-category').style.display = 'block';
    } else {
        document.getElementById(category + '-jobs').style.display = 'block';
    }
    
    if (event && event.target) {
        event.target.classList.add('active');
    }
}

// Legacy function for backward compatibility
function showJobCategory(category, event) {
    showCategory(category, event);
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
        'gambler': '🎰 겜블러',
        'assassin': '🗡️ 암살자',
        'knight': '🛡️ 기사',
        'ninja': '🥷 닌자',
        'gunslinger': '🔫 건슬링거',
        'sniper': '🎯 저격수',
        'masterarcher': '🏹 명사수',
        'crossbowman': '🎯 석궁사수',
        'spearman': '🔱 창술사',
        'trickster': '🃏 트릭스터',
        'poacher': '🦌 밀렵꾼',
        'archmage': '🧙 마도사',
        'barriermage': '🛡️ 결계술사',
        'magicswordsman': '⚔️ 마검사',
        'summoner': '🐉 소환술사',
        'alchemist': '⚗️ 연금술사',
        'lightpriest': '✨ 빛의 사제',
        'darkpriest': '🌑 어둠의 사제',
        'lightningpriest': '⚡ 번개의 사제',
        'soulpriest': '👻 영혼의 사제',
        'timepriest': '⏰ 시간의 사제',
        'dicejob': '🎲 주사위'
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
    const level = parseInt(document.getElementById('archer-level').value) || 1;
    
    let body;
    let endpoint = skill;
    
    if (skill === 'plain' || skill === 'dash' || skill === 'quickshot') {
        body = { stat, level };
    } else if (skill === 'plain-dual' || skill === 'dash-dual') {
        body = { strength, dexterity, level };
    } else if (skill === 'hunt') {
        body = { stat, consecutiveHits, level };
    } else if (skill === 'hunt-dual') {
        body = { strength, dexterity, consecutiveHits, level };
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
    const level = parseInt(document.getElementById('rogue-level').value) || 1;
    
    let body;
    
    if (skill === 'plain') {
        body = { stat, useTwoDice, level };
    } else if (skill === 'stab') {
        body = { stat, level };
    } else if (skill === 'throw') {
        body = { dexterity, swiftness, level };
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
    const maxHP = parseInt(document.getElementById('samurai-maxHP').value) || 100;
    const currentHP = parseInt(document.getElementById('samurai-currentHP').value) || 100;
    const consumedStamina = parseInt(document.getElementById('samurai-consumedStamina').value) || 0;
    const level = parseInt(document.getElementById('samurai-level').value) || 1;
    const isMula = document.getElementById('samurai-isMula').checked;
    const kakugo = document.getElementById('samurai-kakugo').checked;
    const seishaKetsudan = document.getElementById('samurai-seishaKetsudan').checked;
    const scatteringSwordDance = document.getElementById('samurai-scatteringSwordDance').checked;
    
    let body = { stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, scatteringSwordDance, level };
    
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
    const level = parseInt(document.getElementById('berserker-level').value) || 1;
    
    try {
        const response = await fetch(`${API_BASE}/berserker/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, maxHealth, currentHealth, level })
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

// Assassin calculations
async function calculateAssassin(skill) {
    const stat = parseInt(document.getElementById('assassin-stat').value) || 10;
    const isReturnTurn = document.getElementById('assassin-isReturnTurn').checked;
    const isFirstAssault = document.getElementById('assassin-isFirstAssault').checked;
    const isConfirmKillActive = document.getElementById('assassin-isConfirmKillActive').checked;
    
    try {
        const response = await fetch(`${API_BASE}/assassin/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isReturnTurn, isFirstAssault, isConfirmKillActive })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '암살자 - ' + getSkillName('assassin', skill));
        addLog(`🗡️ 암살자 - ${getSkillName('assassin', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Knight calculations
async function calculateKnight(skill) {
    const stat = parseInt(document.getElementById('knight-stat').value) || 10;
    const level = parseInt(document.getElementById('knight-level').value) || 1;
    
    try {
        const response = await fetch(`${API_BASE}/knight/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, level })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '기사 - ' + getSkillName('knight', skill));
        addLog(`🛡️ 기사 - ${getSkillName('knight', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Ninja calculations
async function calculateNinja(skill) {
    const str = parseInt(document.getElementById('ninja-str').value) || 10;
    const dex = parseInt(document.getElementById('ninja-dex').value) || 10;
    const speed = parseInt(document.getElementById('ninja-speed').value) || 10;
    const shurikenCount = parseInt(document.getElementById('ninja-shurikenCount').value) || 1;
    const stealthActive = document.getElementById('ninja-stealthActive').checked;
    const doppelgangerActive = document.getElementById('ninja-doppelgangerActive').checked;
    const ideologySealActive = document.getElementById('ninja-ideologySealActive').checked;
    const resistanceType = document.getElementById('ninja-resistanceType').value;

    try {
        const response = await fetch(`${API_BASE}/ninja/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ str, dex, speed, shurikenCount, stealthActive, doppelgangerActive, ideologySealActive, resistanceType })
        });

        const data = await response.json();
        showDamageResult(data.damage, '닌자 - ' + getSkillName('ninja', skill));
        addLog(`🥷 닌자 - ${getSkillName('ninja', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Gunslinger calculations
async function calculateGunslinger(skill) {
    const stat = parseInt(document.getElementById('gunslinger-stat').value) || 10;
    const swiftness = parseInt(document.getElementById('gunslinger-swiftness').value) || 10;
    const isFirstShot = document.getElementById('gunslinger-isFirstShot').checked;
    const dodgedLastTurn = document.getElementById('gunslinger-dodgedLastTurn').checked;
    const isJudgeTurn = document.getElementById('gunslinger-isJudgeTurn').checked;
    const isJudgementTarget = document.getElementById('gunslinger-isJudgementTarget').checked;
    const isBackstabActive = document.getElementById('gunslinger-isBackstabActive')?.checked || false;
    
    try {
        const response = await fetch(`${API_BASE}/gunslinger/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, swiftness, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, isBackstabActive })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '건슬링거 - ' + getSkillName('gunslinger', skill));
        addLog(`🔫 건슬링거 - ${getSkillName('gunslinger', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Sniper calculations
async function calculateSniper(skill) {
    const stat = parseInt(document.getElementById('sniper-stat').value) || 10;
    const numBuffs = parseInt(document.getElementById('sniper-numBuffs').value) || 0;
    const notAttackedFor5Turns = document.getElementById('sniper-notAttackedFor5Turns').checked;
    const noBasicAttackUsed = document.getElementById('sniper-noBasicAttackUsed').checked;
    const isSecured = document.getElementById('sniper-isSecured')?.checked || false;
    const isSureHit = document.getElementById('sniper-isSureHit')?.checked || false;
    const isAssembled = document.getElementById('sniper-isAssembled')?.checked || false;
    const isAimed = document.getElementById('sniper-isAimed')?.checked || false;
    const isStabilized = document.getElementById('sniper-isStabilized')?.checked || false;
    const isImmersed = document.getElementById('sniper-isImmersed')?.checked || false;
    const isConfident = document.getElementById('sniper-isConfident')?.checked || false;
    const isLoaded = document.getElementById('sniper-isLoaded')?.checked || false;
    const isNerveMax = document.getElementById('sniper-isNerveMax')?.checked || false;

    const turnsSinceAttack = notAttackedFor5Turns ? 5 : 0;

    try {
        const response = await fetch(`${API_BASE}/sniper/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                stat, numBuffs, turnsSinceAttack,
                deathBullet: noBasicAttackUsed,
                secure: isSecured,
                assemble: isAssembled,
                load: isLoaded,
                aim: isAimed,
                sureHit: isSureHit,
                stabilize: isStabilized,
                immersion: isImmersed,
                conviction: isConfident,
                heightenedSenses: isNerveMax
            })
        });

        const data = await response.json();
        showDamageResult(data.damage, '저격수 - ' + getSkillName('sniper', skill));
        addLog(`🎯 저격수 - ${getSkillName('sniper', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// MasterArcher calculations
async function calculateMasterArcher(skill) {
    const stat = parseInt(document.getElementById('masterarcher-stat').value) || 10;
    const isHeavyString = document.getElementById('masterarcher-isHeavyString').checked;
    const isFirstTarget = document.getElementById('masterarcher-isFirstTarget').checked;
    const isEmergencyShot = document.getElementById('masterarcher-isEmergencyShot')?.checked || false;
    
    // If emergency shot checkbox is checked and basic attack is requested, use emergency-shot endpoint
    let actualSkill = skill;
    if (isEmergencyShot && skill === 'plain') {
        actualSkill = 'emergency-shot';
    }
    
    try {
        const response = await fetch(`${API_BASE}/masterarcher/${actualSkill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isHeavyString, isFirstTarget })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '명사수 - ' + getSkillName('masterarcher', actualSkill));
        addLog(`🏹 명사수 - ${getSkillName('masterarcher', actualSkill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Crossbowman calculations
async function calculateCrossbowman(skill) {
    const stat = parseInt(document.getElementById('crossbowman-stat').value) || 10;
    const arrows = parseInt(document.getElementById('crossbowman-arrows').value) || 1;
    const executionArrows = parseInt(document.getElementById('crossbowman-executionArrows').value) || 0;
    const arrowsToBreak = parseInt(document.getElementById('crossbowman-arrowsToBreak').value) || 1;
    const damageTaken = parseInt(document.getElementById('crossbowman-damageTaken').value) || 10;
    const focusedAttack = document.getElementById('crossbowman-focusedAttack').checked;
    const isErrorRemoval = document.getElementById('crossbowman-isErrorRemoval').checked;
    const isDistanceCalc = document.getElementById('crossbowman-isDistanceCalc').checked;
    const isExecutionArrow = document.getElementById('crossbowman-isExecutionArrow').checked;
    
    try {
        const response = await fetch(`${API_BASE}/crossbowman/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                stat, arrows, executionArrows, arrowsToBreak, damageTaken, 
                focusedAttack, isErrorRemoval, isDistanceCalc, isExecutionArrow 
            })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '석궁사수 - ' + getSkillName('crossbowman', skill));
        addLog(`🎯 석궁사수 - ${getSkillName('crossbowman', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Spearman calculations
async function calculateSpearman(skill) {
    const stat = parseInt(document.getElementById('spearman-stat').value) || 10;
    const level = parseInt(document.getElementById('spearman-level').value) || 1;
    const isAdaptationActive = document.getElementById('spearman-isAdaptationActive').checked;
    const isSplendorActive = document.getElementById('spearman-isSplendorActive').checked;
    const splendorTurns = parseInt(document.getElementById('spearman-splendorTurns').value) || 0;
    const isAccelerationActive = document.getElementById('spearman-isAccelerationActive').checked;
    const linkSuccessCount = parseInt(document.getElementById('spearman-linkSuccessCount').value) || 0;
    
    try {
        const response = await fetch(`${API_BASE}/spearman/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isAdaptationActive, isSplendorActive, splendorTurns, isAccelerationActive, linkSuccessCount, level })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '창술사 - ' + getSkillName('spearman', skill));
        addLog(`🔱 창술사 - ${getSkillName('spearman', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Trickster calculations
async function calculateTrickster(skill) {
    const stat = parseInt(document.getElementById('trickster-stat').value) || 10;
    const skillUsedCount = parseInt(document.getElementById('trickster-skillUsedCount').value) || 0;
    const isFocusedFire = document.getElementById('trickster-isFocusedFire').checked;
    const isRepeatCustomer = document.getElementById('trickster-isRepeatCustomer').checked;
    const isEventPrepared = document.getElementById('trickster-isEventPrepared').checked;
    const isMainEvent = document.getElementById('trickster-isMainEvent').checked;
    const isGiantScarActive = document.getElementById('trickster-isGiantScarActive').checked;
    const oilHit = document.getElementById('trickster-oilHit').checked;
    
    try {
        const response = await fetch(`${API_BASE}/trickster/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                stat, skillUsedCount, isFocusedFire, isRepeatCustomer, 
                isEventPrepared, isMainEvent, isGiantScarActive, oilHit 
            })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '트릭스터 - ' + getSkillName('trickster', skill));
        addLog(`🃏 트릭스터 - ${getSkillName('trickster', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Poacher calculations
async function calculatePoacher(skill) {
    const stat = parseInt(document.getElementById('poacher-stat').value) || 10;
    const hasDebuff = document.getElementById('poacher-hasDebuff').checked;
    const isLoaded = document.getElementById('poacher-isLoaded').checked;
    const isWeaknessActive = document.getElementById('poacher-isWeaknessActive').checked;
    
    try {
        const response = await fetch(`${API_BASE}/poacher/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, hasDebuff, isLoaded, isWeaknessActive })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '밀렵꾼 - ' + getSkillName('poacher', skill));
        addLog(`🦌 밀렵꾼 - ${getSkillName('poacher', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Archmage calculations
async function calculateArchmage(skill) {
    const intelligence = parseInt(document.getElementById('archmage-intelligence').value) || 10;
    const usedManaCirculation = document.getElementById('archmage-usedManaCirculation').checked;
    const usedMagicConcentration = document.getElementById('archmage-usedMagicConcentration').checked;
    
    try {
        const response = await fetch(`${API_BASE}/archmage/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, usedManaCirculation, usedMagicConcentration })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '마도사 - ' + getSkillName('archmage', skill));
        addLog(`🧙 마도사 - ${getSkillName('archmage', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

async function calculateArchmageDefense() {
    const baseChantTime = parseInt(document.getElementById('archmage-baseChantTime').value) || 10;
    const remainingChantTime = parseInt(document.getElementById('archmage-remainingChantTime').value) || 5;
    const damageTaken = parseInt(document.getElementById('archmage-damageTaken').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/archmage/rampage-aura`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ baseChantTime, remainingChantTime, damageTaken })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '마도사 - 폭주오라 (감소된 데미지)');
        addLog(`🧙 마도사 - 폭주오라`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// BarrierMage calculations
async function calculateBarrierMage(skill) {
    const selectedBarrierCount = parseInt(document.getElementById('barriermage-selectedBarrierCount').value) || 1;
    const manaSpentOnBarrier = parseInt(document.getElementById('barriermage-manaSpentOnBarrier').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/barriermage/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ selectedBarrierCount, manaSpentOnBarrier })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '결계술사 - ' + getSkillName('barriermage', skill));
        addLog(`🛡️ 결계술사 - ${getSkillName('barriermage', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// MagicSwordsman calculations
async function calculateMagicSwordsman(skill) {
    const intelligence = parseInt(document.getElementById('magicswordsman-intelligence').value) || 10;
    const manaSpentInPreviousAction = parseInt(document.getElementById('magicswordsman-manaSpent').value) || 5;
    const damageTaken = parseInt(document.getElementById('magicswordsman-damageTaken').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/magicswordsman/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, manaSpentInPreviousAction, damageTaken })
        });
        
        const data = await response.json();
        const label = skill === 'flow-aura' ? '마검사 - 플로우 오라 (감소된 데미지)' : '마검사 - ' + getSkillName('magicswordsman', skill);
        showDamageResult(data.damage, label);
        addLog(`⚔️ 마검사 - ${getSkillName('magicswordsman', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Summoner calculations
async function calculateSummoner(skill) {
    const intelligence = parseInt(document.getElementById('summoner-intelligence').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/summoner/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '소환술사 - ' + getSkillName('summoner', skill));
        addLog(`🐉 소환술사 - ${getSkillName('summoner', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Alchemist calculations
function getAlchemistBody() {
    return {
        stat:                   parseInt(document.getElementById('alchemist-stat').value) || 10,
        level:                  parseInt(document.getElementById('alchemist-level').value) || 1,
        precision:              parseInt(document.getElementById('alchemist-precision').value) || 0,
        flasks:                 parseInt(document.getElementById('alchemist-flasks').value) || 0,
        fusionCount:            parseInt(document.getElementById('alchemist-fusionCount').value) || 0,
        corrosionAmpBonus:      parseInt(document.getElementById('alchemist-corrosionAmp').value) || 0,
        poisonAmpBonus:         parseInt(document.getElementById('alchemist-poisonAmp').value) || 0,
        fireAmpBonus:           parseInt(document.getElementById('alchemist-fireAmp').value) || 0,
        iceAmpBonus:            parseInt(document.getElementById('alchemist-iceAmp').value) || 0,
        allAmpBonus:            parseInt(document.getElementById('alchemist-allAmp').value) || 0,
        externalDmgIncrease:    parseInt(document.getElementById('alchemist-externalDmg').value) || 0,
        externalFinalDmgIncrease: parseInt(document.getElementById('alchemist-externalFinalDmg').value) || 0
    };
}

async function calculateAlchemist(skill) {
    const body = getAlchemistBody();

    try {
        const response = await fetch(`${API_BASE}/alchemist/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        const data = await response.json();
        showDamageResult(data.damage, '연금술사 - ' + getSkillName('alchemist', skill));
        addLog(`⚗️ 연금술사 - ${getSkillName('alchemist', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

async function calculateAlchemistFusion(skill, requiredCount) {
    const selectorClass = skill === 'reaction' ? 'reaction-el' : 'chain-el';
    const checked = Array.from(document.querySelectorAll(`#alchemist-skills .${selectorClass}:checked`));
    const elements = checked.map(cb => cb.value);

    if (elements.length !== requiredCount) {
        alert(`정확히 ${requiredCount}가지 속성을 선택해 주세요. (현재 ${elements.length}가지 선택됨)`);
        return;
    }

    const body = Object.assign(getAlchemistBody(), { elements });

    try {
        const response = await fetch(`${API_BASE}/alchemist/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        const data = await response.json();
        showDamageResult(data.damage, '연금술사 - ' + getSkillName('alchemist', skill));
        addLog(`⚗️ 연금술사 - ${getSkillName('alchemist', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}


// LightPriest calculations
async function calculateLightPriest(skill) {
    const intelligence = parseInt(document.getElementById('lightpriest-intelligence').value) || 10;
    const level = parseInt(document.getElementById('lightpriest-level').value) || 1;
    const healBonus = parseInt(document.getElementById('lightpriest-healBonus').value) || 0;
    const finalHealBonus = parseInt(document.getElementById('lightpriest-finalHealBonus').value) || 0;
    const chantTurns = parseInt(document.getElementById('lightpriest-chantTurns').value) || 1;
    const precision = parseInt(document.getElementById('lightpriest-precision').value) || 0;
    const hasAttacked = document.getElementById('lightpriest-hasAttacked').checked;
    const favoritism = document.getElementById('lightpriest-favoritism').checked;
    const transfer = document.getElementById('lightpriest-transfer').checked;
    const piety = document.getElementById('lightpriest-piety').checked;

    try {
        const response = await fetch(`${API_BASE}/lightpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, level, healBonus, finalHealBonus, chantTurns,
                precision, hasAttacked, favoritism, transfer, piety })
        });

        const data = await response.json();
        showDamageResult(data.damage, '빛의 사제 - ' + getSkillName('lightpriest', skill));
        addLog(`✨ 빛의 사제 - ${getSkillName('lightpriest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// DarkPriest calculations
async function calculateDarkPriest(skill) {
    const intelligence = parseInt(document.getElementById('darkpriest-intelligence').value) || 10;
    const level = parseInt(document.getElementById('darkpriest-level').value) || 1;
    const damageBonus = parseInt(document.getElementById('darkpriest-damageBonus').value) || 0;
    const finalDamageBonus = parseInt(document.getElementById('darkpriest-finalDamageBonus').value) || 100;
    const precision = parseInt(document.getElementById('darkpriest-precision').value) || 0;
    const stolenHp = parseInt(document.getElementById('darkpriest-stolenHp').value) || 0;
    const erosionAllies = parseInt(document.getElementById('darkpriest-erosionAllies').value) || 0;
    const pietyAllies = parseInt(document.getElementById('darkpriest-pietyAllies').value) || 0;
    const scapegoatHit = parseInt(document.getElementById('darkpriest-scapegoatHit').value) || 0;
    const domination = document.getElementById('darkpriest-domination').checked;
    const bloodflow = document.getElementById('darkpriest-bloodflow').checked;

    try {
        const response = await fetch(`${API_BASE}/darkpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, level, damageBonus, finalDamageBonus,
                precision, domination, stolenHp, bloodflow, scapegoatHit,
                erosionAllies, pietyAllies })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '어둠의 사제 - ' + getSkillName('darkpriest', skill));
        addLog(`🌑 어둠의 사제 - ${getSkillName('darkpriest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// LightningPriest calculations
async function calculateLightningPriest(skill) {
    const intelligence = parseInt(document.getElementById('lightningpriest-intelligence').value) || 10;
    const level = parseInt(document.getElementById('lightningpriest-level').value) || 1;
    const damageBonus = parseInt(document.getElementById('lightningpriest-damageBonus').value) || 0;
    const finalDamageBonusEl = document.getElementById('lightningpriest-finalDamageBonus');
    const finalDamageBonus = finalDamageBonusEl.value !== '' ? parseInt(finalDamageBonusEl.value) : 100;
    const chantTurns = parseInt(document.getElementById('lightningpriest-chantTurns').value) || 1;
    const precision = parseInt(document.getElementById('lightningpriest-precision').value) || 0;
    const monopoly = document.getElementById('lightningpriest-monopoly').checked;
    const blessing = document.getElementById('lightningpriest-blessing').checked;
    const piety = document.getElementById('lightningpriest-piety').checked;
    const elraister = document.getElementById('lightningpriest-elraister').checked;

    try {
        const response = await fetch(`${API_BASE}/lightningpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, level, damageBonus, finalDamageBonus,
                chantTurns, precision, monopoly, blessing, piety, elraister })
        });

        const data = await response.json();
        showDamageResult(data.damage, '번개의 사제 - ' + getSkillName('lightningpriest', skill));
        addLog(`⚡ 번개의 사제 - ${getSkillName('lightningpriest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// SoulPriest calculations
async function calculateSoulPriest(skill) {
    const intelligence = parseInt(document.getElementById('soulpriest-intelligence').value) || 10;
    const soul = parseInt(document.getElementById('soulpriest-soul').value) || 0;
    const soulsSpent = parseInt(document.getElementById('soulpriest-soulsSpent').value) || 5;
    const level = parseInt(document.getElementById('soulpriest-level').value) || 1;
    const damageBonus = parseInt(document.getElementById('soulpriest-damageBonus').value) || 0;
    const finalDamageBonus = parseInt(document.getElementById('soulpriest-finalDamageBonus').value) || 100;
    const precision = parseInt(document.getElementById('soulpriest-precision').value) || 0;
    const ruins = document.getElementById('soulpriest-ruins').checked;

    try {
        const response = await fetch(`${API_BASE}/soulpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, soul, ruins, soulUse: soulsSpent, level, damageBonus, finalDamageBonus, precision })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '영혼의 사제 - ' + getSkillName('soulpriest', skill));
        addLog(`👻 영혼의 사제 - ${getSkillName('soulpriest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// TimePriest calculations
async function calculateTimePriest(skill) {
    const intelligence = parseInt(document.getElementById('timepriest-intelligence').value) || 10;
    
    try {
        const response = await fetch(`${API_BASE}/timepriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '시간의 사제 - ' + getSkillName('timepriest', skill));
        addLog(`⏰ 시간의 사제 - ${getSkillName('timepriest', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// DiceJob calculations
async function calculateDiceJob() {
    const stat = parseInt(document.getElementById('dicejob-stat').value) || 10;
    const dices = parseInt(document.getElementById('dicejob-dices').value) || 1;
    const sides = parseInt(document.getElementById('dicejob-sides').value) || 6;

    try {
        const response = await fetch(`${API_BASE}/dicejob/roll`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, dices, sides })
        });

        const data = await response.json();
        showDamageResult(data.damage, `🎲 주사위 - ${dices}D${sides} 판정`);
        addLog(`🎲 주사위 - ${dices}D${sides} 판정 (스탯: ${stat})`, data.log);
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
            'hunt': '사냥감 (단일)',
            'hunt-dual': '사냥감 (동시)'
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
            'last-strike': '최후의 일격',
            'devastating-blow': '파멸의 일격'
        },
        gambler: {
            'plain': '기본공격',
            'coin-toss': '코인 토스',
            'joker-card': '조커 카드',
            'blackjack': '블랙잭',
            'yatzy-dice': '야추 다이스',
            'royal-flush': '로얄 플러쉬'
        },
        assassin: {
            'plain': '기본공격',
            'assassinate': '암살',
            'critical-stab': '급소 찌르기',
            'throat-slit': '목 긋기',
            'wrist-slit': '손목 긋기',
            'rear-attack': '후방 공격'
        },
        knight: {
            'plain': '기본공격',
            'smash-down': '내려치기',
            'sweep': '후려치기',
            'head-strike': '머리치기',
            'defense-break': '수비파괴',
            'stun': '기절시키기',
            'critical-strike': '일격'
        },
        ninja: {
            'strike': '일격',
            'mangle': '난도',
            'throw': '투척 표창',
            'phantom-dance': '환영난무',
            'focused-throw': '일점투척'
        },
        gunslinger: {
            'plain': '기본공격',
            'double-shot': '더블샷',
            'headshot': '헤드샷',
            'quick-draw': '퀵드로우',
            'focus-fire': '일점사',
            'backstab': '백스탭',
            'warning': '경고',
            'notice': '예고장',
            'active-opportunity': '활약 기회'
        },
        sniper: {
            'plain': '기본공격',
            'secure': '확보',
            'assemble': '조립',
            'load': '장전',
            'aim': '조준',
            'fire': '발사',
            'stabilize': '안정화',
            'immerse': '몰입',
            'confidence': '확신',
            'nerve-max': '신경 극대화'
        },
        masterarcher: {
            'plain': '기본공격',
            'emergency-shot': '긴급사격',
            'power-shot': '파워샷',
            'explosive-arrow': '폭탄 화살',
            'split-arrow': '분열 화살',
            'piercing-arrow': '관통 화살',
            'double-shot': '더블 샷'
        },
        crossbowman: {
            'plain': '기본공격',
            'throw': '던지기',
            'quick-load': '빠른 장전',
            'single-shot': '단일사격',
            'rage-arrow': '발광 화살',
            'paralyze-arrow': '마비 화살',
            'break-arrows': '화살 꺾기',
            'desperate-load': '이럴 때 일수록!'
        },
        spearman: {
            'plain': '기본공격',
            'spin-thrust': '돌려 찌르기',
            'spin-strike': '회전 타격',
            'low-slash': '하단 베기',
            'combo-front-thrust': '[연계]정면 찌르기',
            'combo-flash-spear': '[연계]일섬창',
            'combo-thunder-strike': '[연계]천뢰격'
        },
        trickster: {
            'plain': '기본공격',
            'fake-dagger': '페이크 단검',
            'bean-shot': '콩알탄',
            'oil-barrel': '기름통 투척',
            'lighter-throw': '라이터 투척',
            'huge-dagger': '특대형 단검',
            'party-time': '파티 타임',
            'event-preparation': '이벤트 준비',
            'giant-scar': '거대한 상흔',
            'main-event': '메인 이벤트'
        },
        poacher: {
            'plain': '기본공격',
            'head-chop': '머리찍기',
            'set-trap': '덫 깔기',
            'snare-shot': '올가미 탄',
            'headshot': '헤드샷'
        },
        archmage: {
            'magic-bolt': '마력탄',
            'ether-catastrophe': '에테르 카타스트로피',
            'lumen-conversion-aoe': '루멘 컨버전 (광역)',
            'lumen-conversion-single': '루멘 컨버전 (단일)',
            'rampage-aura': '폭주오라'
        },
        barriermage: {
            'force-field-barrier': '역장 결계',
            'barrier-afterimage': '결계 잔영',
            'energy-recovery': '기운 회수'
        },
        magicswordsman: {
            'plain': '기본공격',
            'mana-slash': '마나 슬래쉬',
            'mana-strike': '마나 스트라이크',
            'mana-spear': '마나 스피어',
            'spin-chryst': '스핀 크라이스트',
            'triple-slain': '트리플 슬레인',
            'ethereal-imperio': '에테리얼 임페리오',
            'speed-drain': '스피드레인',
            'flow-aura': '플로우 오라'
        },
        summoner: {
            'plain': '기본공격',
            'punch-to-beat-summon': '소환수를 이기는 주먹',
            'punch-to-obey': '말을 잘 듣게 하는 주먹'
        },
        alchemist: {
            'plain': '기본 공격',
            'vitriolic-flask': '비트리올 플라스크',
            'blight-flask': '블라이트 플라스크',
            'ignis-flask': '이그니스 플라스크',
            'absolute-flask': '앱솔루트 플라스크',
            'arcane-flask': '아케인 플라스크',
            'reaction': '리액션',
            'chain-distortion': '체인 디스토션',
            'matter-disaster': '매터 디재스티아'
        },
        lightpriest: {
            'plain': '기본공격',
            'heal': '힐',
            'healing-wind': '치유의 바람',
            'chalice-of-light': '빛의 성배',
            'invocation': '기원',
            'prayer': '기도',
            'heavens-door': '헤븐즈 도어'
        },
        darkpriest: {
            'plain': '기본공격',
            'dark-energy': '어둠의 기운',
            'grip': '손아귀',
            'uzumania': '우즈마니아',
            'exilister': '엑실리스터',
            'annihilation-plain': '어나이스필레인',
            'ensiasticalia': '엔시아스티켈리아'
        },
        lightningpriest: {
            'plain': '기본공격',
            'spark': '스파크',
            'chain-lightning-damage': '체인 라이트닝 (공격)',
            'chain-lightning-shield': '체인 라이트닝 (보호막)',
            'electric-field': '일렉트릭 필드',
            'strike': '스트라이크',
            'elraister': '일레이스터',
            'divine-lightning': '신뇌격'
        },
        soulpriest: {
            'plain': '기본공격',
            'absorb': '흡수',
            'curse': '저주',
            'chest-pain': '흉통',
            'grudge': '원한',
            'collect': '수거'
        },
        timepriest: {
            'plain': '기본공격',
            'corrosion': '부식'
        },
        dicejob: {
            'roll': '판정 시도'
        }
    };
    
    var jobSkills = skillNames[job];
    if (jobSkills && jobSkills[skill]) {
        return jobSkills[skill];
    }
    return skill;
}

// Defense calculations
async function calculateDefense(defenseType) {
    const damageTaken = parseInt(document.getElementById('defense-damageTaken').value) || 50;
    const strength = parseInt(document.getElementById('defense-strength').value) || 10;
    const dexterity = parseInt(document.getElementById('defense-dexterity').value) || 10;
    const swiftness = parseInt(document.getElementById('defense-swiftness').value) || 10;
    const critical = parseInt(document.getElementById('defense-critical').value) || 10;
    
    let requestBody = { damageTaken };
    
    switch(defenseType) {
        case 'defend':
            requestBody.strength = strength;
            break;
        case 'evade':
            requestBody.dexterity = dexterity;
            break;
        case 'deflect':
            requestBody.strength = strength;
            requestBody.dexterity = dexterity;
            break;
        case 'parry':
            requestBody.strength = strength;
            requestBody.dexterity = dexterity;
            requestBody.swiftness = swiftness;
            break;
        case 'counter-parry':
            requestBody.strength = strength;
            requestBody.dexterity = dexterity;
            requestBody.swiftness = swiftness;
            requestBody.critical = critical;
            break;
    }
    
    try {
        const response = await fetch(`${API_BASE}/defense/${defenseType}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        const data = await response.json();
        
        const defenseNames = {
            'defend': '방어',
            'evade': '회피',
            'deflect': '흘리기',
            'parry': '패링',
            'counter-parry': '카운터 패링'
        };
        
        document.getElementById('defenseResult').innerHTML = `
            <div class="damage-label">🛡️ ${defenseNames[defenseType]} - 최종 받는 데미지</div>
            <div class="damage-value">${data.damage}</div>
        `;
        
        addLog(`🛡️ 수비-${defenseNames[defenseType]} (받은 데미지: ${damageTaken})`, data.log);
    } catch (error) {
        console.error('Error:', error);
        alert('수비 계산 중 오류가 발생했습니다.');
    }
}

// Essence calculations
async function calculateEssence(essenceType) {
    const baseDamage = parseInt(document.getElementById('essence-baseDamage').value) || 100;
    const durationTurns = parseInt(document.getElementById('essence-durationTurns').value) || 3;
    const last3TurnsDamage = parseInt(document.getElementById('essence-last3TurnsDamage').value) || 150;
    const allyDamage = parseInt(document.getElementById('essence-allyDamage').value) || 50;
    
    let requestBody = { baseDamage };
    
    switch(essenceType) {
        case 'surge':
        case 'rampage':
        case 'lightning-proxy':
            requestBody.durationTurns = durationTurns;
            break;
        case 'afterglow':
            requestBody.last3TurnsDamage = last3TurnsDamage;
            break;
        case 'dark-proxy':
            requestBody.allyDamage = allyDamage;
            break;
    }
    
    try {
        const response = await fetch(`${API_BASE}/essence/${essenceType}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        const data = await response.json();
        
        const essenceNames = {
            'sunset': '석양',
            'black-flame': '흑염',
            'afterglow': '잔향',
            'thunder': '천둥',
            'surge': '격동',
            'flash': '섬광',
            'rampage': '폭주',
            'light-proxy': '빛(대리자)',
            'dark-proxy': '어둠(대리자)',
            'soul-proxy': '영혼(대리자)',
            'lightning-proxy': '번개(대리자)'
        };
        
        document.getElementById('essenceResult').innerHTML = `
            <div class="damage-label">✨ 정수-${essenceNames[essenceType]} - 최종 데미지</div>
            <div class="damage-value">${data.damage}</div>
        `;
        
        addLog(`✨ 정수-${essenceNames[essenceType]} (기본: ${baseDamage})`, data.log);
    } catch (error) {
        console.error('Error:', error);
        alert('정수 계산 중 오류가 발생했습니다.');
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    addLog('🎮 TRPG 데미지 계산기가 시작되었습니다!');

    // Prevent skill buttons from submitting any enclosing form so that
    // input values typed within the same job are preserved when switching skills.
    document.querySelectorAll('.skill-form .btn-skill').forEach(btn => {
        btn.addEventListener('click', e => e.preventDefault());
    });

    // Lightning priest monopoly checkbox (no additional toggle needed - skills shown always)
    const monopolyCheck = document.getElementById('lightningpriest-monopoly');
    if (monopolyCheck) {
        // No extra UI to toggle - skill checkboxes are always visible and serve dual purpose
    }
});
