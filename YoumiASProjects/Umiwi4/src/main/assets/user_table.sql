create:
CREATE TABLE IF NOT EXISTS [user_table] 
	([uid] TEXT NOT NULL ON CONFLICT REPLACE, 
	[username] TEXT,
	[mobile] TEXT,
	[avatar] TEXT,
	[balance] TEXT,
	[identity] TEXT,
	[grade] TEXT,  
	[identityexpire] TEXT,
	[tutoruid] TEXT, 
	[account] TEXT,
	[price] TEXT,
	[usertest] TEXT,
	[mycoin] TEXT,
	[cookie] TEXT,
	[loginStatus] INT);
---------------------------------------