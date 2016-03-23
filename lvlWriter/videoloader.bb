; Movie Commands Example
; ======================
; This demonstrates the following commands:
;
;	OpenMovie
;	MovieHeight
;	MovieWidth
;	MoviePlaying
;	DrawMovie
Include "lvlWriter.bb"
;SaveLevel()

;Unpack("movieembedtest.exe")
movie=LoadLevel("movieembedtest.exe")


; Some constants to start with
Const WIDTH = 1024
Const HEIGHT = 768
Global moviename$
; First of all, set up the graphics
Graphics WIDTH, HEIGHT,32,1

ClsColor 0,0,0
Color 0,255,0

; Next, open the movie file.  Feel free to change this to an AVI or MPEG file.

movie=OpenMovie(moviename$)
Repeat 
DrawMovie movie,0,0,WIDTH,HEIGHT

Until KeyHit(1)

; Remove the movie from memory before closing down
CloseMovie(movie)
DeleteFile (moviename$)
End ; bye!


Function SaveLevel(f_name$="movie.dat")
    file=WriteFile(f_name$)
	lvlSetFile(file)
    lvlBeginChunk("VOID") ;file header
    	lvlWriteInt(1) ;version number
		lvlWriteString(CurrentDate$()) 
		lvlWriteString(CurrentTime$())
		lvlWriteString("Movie Data: Full Moon Wo Sagashite")		
    lvlEndChunk()


vid_name$="video"
vid_file=ReadFile(vid_name$)
   lvlBeginChunk("EMBD")
		lvlBeginChunk("MPGS")
			lvlWriteString(vid_name$)
			lvlBeginChunk("DATA")
			While Not Eof(vid_file)
				lvlWriteByte(ReadByte(vid_file))
			Wend
			CloseFile(vid_file)
			lvlEndChunk()
		lvlEndChunk()
   lvlEndChunk()
CloseFile file
End Function 


Function LoadLevel(f_name$)
    file=ReadFile(f_name$)
	lvlSetFile(file)
	Repeat 
    Until lvlSearchChunk$("VOID") = True
    If lvlReadChunk$() = "VOID"
    	Print "Version: "+lvlReadInt()
		Print "Date Created: "+lvlReadString$() 
		Print "Time Created: "+lvlReadString$()
		Print "Description: "+lvlReadString$()
		Print		
    End If
    lvlExitChunk() 


   If lvlReadChunk$() = "EMBD"
		If lvlReadChunk$() = "MPGS"
			vid_name$=lvlReadString$()
			If lvlReadChunk$() = "DATA"
			    vid_size = lvlChunkSize()
			    vid_file=WriteFile(vid_name$)
				For pos = 1 To vid_size
					WriteByte(vid_file,lvlReadByte())
					Progress(vid_size,pos)
				Next 
				CloseFile(vid_file)
				NameVid(vid_name$)
			End If	
			lvlExitChunk()
		End If	
		lvlExitChunk()
	 EndIf	
   lvlExitChunk()

CloseFile(file)
Return movies
End Function

Function NameVid(name$)
moviename$=name$
End Function

Function Progress(num1#,num2#)
	num#=(num2/num1#)*100
	If num# Mod 5 =0 Then Write num#+"%"
	If num# Mod 2 =0 Then Write "."
	If num# Mod 100 =0 Then Write "DONE"
End Function 




Function Unpack(f_nameExe$)
	If FileType(f_nameExe$)=0: RuntimeError f_nameExe$+" does not exist!":End:EndIf
	exeFile=ReadFile(f_nameExe$)
	lvlFile=WriteFile("movie.dat")
	sizeFile=FileSize(f_nameExe$)
	SeekFile(exeFile,sizeFile-4)
	poslvl=ReadInt(exeFile)
	SeekFile(exeFile,poslvl)
	While Not Eof(exeFile)
		WriteByte(lvlFile,ReadByte(exeFile))
	Wend
	CloseFile(lvlFile)
	CloseFile(exeFile)
End Function