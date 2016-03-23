Include "lvlWriter.bb"

file=OpenFile("default.lvl")
lvlSetFile(file)
Repeat 
Until lvlSearchChunk$("Dvgj") = True
Write Chr$(lvlReadByte())
Write Chr$(lvlReadByte())
Write Chr$(lvlReadByte())
Write Chr$(lvlReadByte())
Write Chr$(lvlReadByte())
Write Chr$(lvlReadByte())

WaitKey()
CloseFile(file)