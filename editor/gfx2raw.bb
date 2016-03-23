gfx2raw("arrow.png")

Function gfx2raw(img$)
	rgb$=""
	dat=WriteFile("data.bb")
	gfx=LoadImage(img$)
	w=ImageWidth(gfx)
	h=ImageHeight(gfx)
	WriteLine(dat,"Data "+w+","+h)
	DrawImage gfx,0,0
	For y=0 To w
		For x=0 To h
			GetColor x+1,y+1
			rgb$=ColorRed()+","+ColorGreen()+","+ColorBlue()
			WriteLine(dat,"Data "+rgb$)
		Next
	Next 
	CloseFile(dat) 
End Function