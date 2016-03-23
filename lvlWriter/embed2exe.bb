AppTitle "Embed2Exe"
wnd = SystemProperty("AppHWND")


fname$=Input("Executable file:")
If FileType(fname$)=0 RuntimeError fname$ +" not found!" 
fdata$=Input("Dependable file:")
If FileType(fdata$)=0 RuntimeError fdata$ +" not found!"

UnpackUPX()
CompressExe(fname$)
While api_GetActiveWindow() <> wnd
	Delay 2000
Wend
DeleteFile "upx.exe"
Embed2Exe("dump.ex",fdata$)
Cleanup(fname$)
Print "Done"
Delay 2000
;Unpack("embed.exe")

End


Function CompressExe(fname$)
	Print "Compressing "+ fname$
	ExecFile "upx.exe -9 -odump.ex"+" "+fname$
End Function

Function Embed2Exe(f_nameExe$,f_name$)
	Delay 3000
	Print "Appending "+ f_name$ 
	exeFile=OpenFile(f_nameExe$)
	lvlFile=ReadFile(f_name$)
	sizeFile=FileSize(f_nameExe$)
	While Not Eof(exeFile)
		ReadByte(exeFile)
	Wend
	While Not Eof(lvlFile)
		WriteByte(exeFile,ReadByte(lvlFile))
	Wend
	WriteInt(exeFile,FileSize(f_nameExe$))
	CloseFile(lvlFile)
	CloseFile(exeFile)
	Delay 2000
End Function

Function UnpackUPX()
	exeFile=ReadFile("embed2exe.exe")
	lvlFile=WriteFile("upx.exe")
	sizeFile=FileSize("embed2exe.exe")
	SeekFile(exeFile,sizeFile-4)
	poslvl=ReadInt(exeFile)
	SeekFile(exeFile,poslvl)
	While Not Eof(exeFile)
		WriteByte(lvlFile,ReadByte(exeFile))
	Wend
	CloseFile(lvlFile)
	CloseFile(exeFile)
	Delay 2500
End Function

Function Cleanup(fname$)
	Print "Cleanup"
	CopyFile "dump.ex",fname$
	DeleteFile "dump.ex"
	Delay 1000
End Function 