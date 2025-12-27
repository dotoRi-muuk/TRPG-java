// TRPG Damage Calculator - Frontend JavaScript

const API_BASE = '/api';
let currentJob = null;

// Show job category (normal/hidden/secret)
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
        'timepriest': '⏰ 시간의 사제'
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

// Assassin calculations
async function calculateAssassin(skill) {
    const stat = parseInt(document.getElementById('assassin-stat').value) || 10;
    const isReturnTurn = document.getElementById('assassin-isReturnTurn').checked;
    const isFirstAssault = document.getElementById('assassin-isFirstAssault').checked;
    
    try {
        const response = await fetch(`${API_BASE}/assassin/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isReturnTurn, isFirstAssault })
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
    
    try {
        const response = await fetch(`${API_BASE}/knight/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat })
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
    const stat = parseInt(document.getElementById('ninja-stat').value) || 10;
    const shurikenCount = parseInt(document.getElementById('ninja-shurikenCount').value) || 1;
    const isIllusionTurn = document.getElementById('ninja-isIllusionTurn').checked;
    const isCloneActive = document.getElementById('ninja-isCloneActive').checked;
    const isReflexActive = document.getElementById('ninja-isReflexActive').checked;
    
    try {
        const response = await fetch(`${API_BASE}/ninja/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, shurikenCount, isIllusionTurn, isCloneActive, isReflexActive })
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
    const isFirstShot = document.getElementById('gunslinger-isFirstShot').checked;
    const dodgedLastTurn = document.getElementById('gunslinger-dodgedLastTurn').checked;
    const isJudgeTurn = document.getElementById('gunslinger-isJudgeTurn').checked;
    
    try {
        const response = await fetch(`${API_BASE}/gunslinger/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isFirstShot, dodgedLastTurn, isJudgeTurn })
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
    
    try {
        const response = await fetch(`${API_BASE}/sniper/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, numBuffs, notAttackedFor5Turns, noBasicAttackUsed })
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
    
    try {
        const response = await fetch(`${API_BASE}/masterarcher/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isHeavyString, isFirstTarget })
        });
        
        const data = await response.json();
        showDamageResult(data.damage, '명궁 - ' + getSkillName('masterarcher', skill));
        addLog(`🏹 명궁 - ${getSkillName('masterarcher', skill)}`, data.log);
    } catch (error) {
        console.error('Error:', error);
        addLog('❌ 오류 발생: ' + error.message);
    }
}

// Crossbowman calculations
async function calculateCrossbowman(skill) {
    const stat = parseInt(document.getElementById('crossbowman-stat').value) || 10;
    const arrows = parseInt(document.getElementById('crossbowman-arrows').value) || 1;
    const arrowsToBreak = parseInt(document.getElementById('crossbowman-arrowsToBreak').value) || 1;
    const damageTaken = parseInt(document.getElementById('crossbowman-damageTaken').value) || 10;
    const focusedAttack = document.getElementById('crossbowman-focusedAttack').checked;
    
    try {
        const response = await fetch(`${API_BASE}/crossbowman/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, arrows, arrowsToBreak, damageTaken, focusedAttack })
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
    
    try {
        const response = await fetch(`${API_BASE}/spearman/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat })
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
    const isFocusedFire = document.getElementById('trickster-isFocusedFire').checked;
    const isRepeatCustomer = document.getElementById('trickster-isRepeatCustomer').checked;
    const hasEventBonus = document.getElementById('trickster-hasEventBonus').checked;
    const oilHit = document.getElementById('trickster-oilHit').checked;
    
    try {
        const response = await fetch(`${API_BASE}/trickster/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, isFocusedFire, isRepeatCustomer, hasEventBonus, oilHit })
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
    
    try {
        const response = await fetch(`${API_BASE}/poacher/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stat, hasDebuff, isLoaded })
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
async function calculateAlchemist(skill) {
    const intelligence = parseInt(document.getElementById('alchemist-intelligence').value) || 10;
    const unknownPotions = parseInt(document.getElementById('alchemist-unknownPotions').value) || 5;
    
    try {
        const response = await fetch(`${API_BASE}/alchemist/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, unknownPotions })
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
    const hasAttacked = document.getElementById('lightpriest-hasAttacked').checked;
    
    try {
        const response = await fetch(`${API_BASE}/lightpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, hasAttacked })
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
    
    try {
        const response = await fetch(`${API_BASE}/darkpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence })
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
    const n = parseInt(document.getElementById('lightningpriest-n').value) || 3;
    
    try {
        const response = await fetch(`${API_BASE}/lightningpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, n })
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
    const soulsSpent = parseInt(document.getElementById('soulpriest-soulsSpent').value) || 5;
    
    try {
        const response = await fetch(`${API_BASE}/soulpriest/${skill}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ intelligence, soulsSpent })
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
            'plain': '기본공격',
            'strike': '일격',
            'chaos': '난도',
            'throw-shuriken': '투척 표창',
            'illusion-barrage': '환영난무',
            'focus-throw': '일점투척'
        },
        gunslinger: {
            'plain': '기본공격',
            'double-shot': '더블샷',
            'headshot': '헤드샷',
            'quick-draw': '퀵드로우',
            'focus-fire': '일점사',
            'backstab': '백스탭'
        },
        sniper: {
            'plain': '기본공격',
            'secure': '확보',
            'assemble': '조립',
            'load': '장전',
            'aim': '조준',
            'fire': '발사'
        },
        masterarcher: {
            'plain': '기본공격',
            'power-shot': '파위샷',
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
            'huge-dagger': '특대형 단검'
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
            'plain': '기본공격',
            'toxic-potion': '독성물약',
            'explosive-potion': '폭발물약',
            'healing-potion': '회복물약',
            'hasty-preparation': '성급한 준비',
            'perfect-preparation': '완벽한 준비'
        },
        lightpriest: {
            'plain': '기본공격',
            'heal': '힐',
            'healing-wind': '치유의 바람',
            'chalice-of-light': '빛의 성배',
            'prayer': '기원',
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
