package com.boray.preView;

public class BlockPreViewOfScen2 {
/*	void HP_Preview_Run_Scan()//PreviewTest2018_12_11A1
	{
		int tmp;
		int time_ms_cnt=0;

//		if (HP_Preview_Status==FALSE) return;
//		if (HP_This_SceneNo_Bak>24) return;	// 
//		if (HP_This_SceneNo_Bak==0) return;	//当为0，强行关；1-16可运行
		//******************************************
//		Fog_Work_Logic_Loop1ms();//雾机控制逻辑函数   
//		if (HP_Setup_Total_Time==0) return;
		//---------------------------
		HP_Preview_ExecutionTime_OneGroup(1);//
		NHP_Preview_ExecutionTime_OneGroup(PreviewGroup);
		//---------------------------
		time_ms_cnt++;
//		if (time_ms_cnt>=1000)//100ms  //
		if(NotHP_TimeOut_1S_Flag==1)
		{
			NotHP_TimeOut_1S_Flag=0;
	//printf("\r\n 手动HP进入=:%d",(int)(1));//
			time_ms_cnt=0;
	   		HP_This_Run_Time_Cnt++;//手动编程运行时间计数累计
			//---------------------
			//---------------------
			//MODI2017_03_20A6
			//如果时间片总时间完成，全部从起点开始运行；
			if (HP_This_Run_Time_Cnt>=HP_Setup_Total_Time)
			{
	//printf("\r\n 时间轴总时间完成:%d",(int)(HP_This_Run_Time_Cnt));
		  		HP_This_Run_Time_Cnt=0;
				HP_Preview_Status=FALSE;
				DLP.PSta=Pause;
				//**************************
			}	
			//---------------------
		}
		//------------------------------------
	}
	
	void HP_Preview_ExecutionTime_OneGroup(int group)//Mask2017_08_31A1
	{
		int tmp;
		int zone_index;
		int work_status;
		//uchar group=1;
		int block;
		int Refresh_dmx512=0;

		int  dy;
		int  TimeDiff;
		int split_step;
		int front_step;
		int front_block;
		int ch;
		int group_lamps;//组灯个数

		//for(tmp=0;tmp<LAMPGROUP_MAX;tmp++)
		//手动编程当前步执行延时值
	    zone_index=group-1;
	    
	    block=1;//HP_This_BlockNo_Tabl[zone_index];//30组 当前运行的哪个时间片《下标》
		//*******************************
		//当为拆分时：取小步函数
		TimeDiff=HP_Setup_SplitIntervalTime_Tabl[zone_index];
		if ( (HP_Cfg_Tabl[zone_index].s.split!=SPLIT_NULL)&&(TimeDiff!=0) )//当为拆分处理
		{
			//拆分小步程序 MODI2016_11_09A5
		    TimeDiff=HP_This_SplitIntervalTime_Tabl[zone_index];

//printf("\r\n 小步TimeDiff时间减=:%d",(int)(TimeDiff));//MODI2016_11_08A4
//printf("\r\n 小步SplitStepNO时间减=:%d",(int)(HP_This_SplitStepNO_Tabl[zone_index]));//MODI2016_11_08A4	

			if (TimeDiff>0) TimeDiff--;
			HP_This_SplitIntervalTime_Tabl[zone_index]=TimeDiff;
			if (TimeDiff==0)//拆分倒计时到时，开始运行1小步；
			{
				Refresh_dmx512=1;
//printf("\r\n 小步执行时间到=:%d",(int)(1));
				//-----------------------------------
				HP_This_SplitIntervalTime_Tabl[zone_index]=HP_Setup_SplitIntervalTime_Tabl[zone_index];
				split_step=HP_This_SplitStepNO_Tabl[zone_index];

				split_step++;

//printf("\r\n 测试当split setup interval=:%d",(int)(HP_Setup_SplitIntervalTime_Tabl[zone_index]));//MODI2016_11_08A4
//printf("\r\n 测试当split cuu step=:%d",(int)(split_step));//MODI2016_11_08A4
//printf("\r\n 测试当split total=:%d",(int)(HP_Setup_SplitStepTotal_Tabl[zone_index]));//MODI2016_11_08A4

				if (split_step>HP_Setup_SplitStepTotal_Tabl[zone_index])
				{
			   		split_step=1;	
				}
				HP_This_SplitStepNO_Tabl[zone_index]=split_step;

//printf("\r\n 测试当split little step=:%d",(int)(split_step));//MODI2016_11_08A4

				//执行拆分步程序
				front_block=PreviewBlock;//HP_This_BlockNo_Tabl[zone_index];
				front_step=HP_This_StepNo_Tabl[zone_index]; //当前运行步骤编号 
				Execution_Split_LittleStep_To_Buf(group,front_block,front_step,HP_playbuf);
				//-----------------------------------
			}
		}

		//*********************************
		//MARK2017_03_01
		//不拆分，时差大于0的情况
		if(HP_Split_TimeDiff_Flag==1)
		if((HP_Cfg_Tabl[zone_index].s.split==SPLIT_NULL)&&(TimeDiff!=0))//不拆分且时差不为0
		{
			//TimeDiff=HP_Convert_TimeDiff(&HP_Cfg_Tabl[zone_index].buffer[2]);
			TimeDiff=HP_This_SplitIntervalTime_Tabl[zone_index];

			if (TimeDiff>0) TimeDiff--;
//printf("\r\n 不拆分，时差大于0TimeDiff=:%d",(int)(TimeDiff));		
			HP_This_SplitIntervalTime_Tabl[zone_index]=TimeDiff;
			
			if(TimeDiff==0)
			{
//printf("\r\n 不拆分，时差大于0=:%d",(int)(1));
				Refresh_dmx512=1;

				HP_This_SplitIntervalTime_Tabl[zone_index]=HP_Setup_SplitIntervalTime_Tabl[zone_index];

				group_lamps=Get_Group_Lamps(group);	//取出组灯个数
//printf("\r\n 组灯个数group_lamps=:%d",(int)(group_lamps));			
				split_step=HP_This_SplitStepNO_Tabl[zone_index];
//printf("\r\n split_step=:%d",(int)(split_step));
				split_step++;			

				if (split_step>group_lamps)
				{
			   		split_step=1;	
				}
				HP_This_SplitStepNO_Tabl[zone_index]=split_step;
//printf("\r\n split_step=:%d",(int)(split_step));	
				//执行拆分步程序
				front_block=HP_This_BlockNo_Tabl[zone_index];
				front_step=HP_This_StepNo_Tabl[zone_index]; //当前运行步骤编号 
				Execution_Split_TimeDiff_To_Buf(group,front_block,front_step,HP_playbuf);
			}		 
		}
		//*********************************
		//*******************************			               
		//检查步时间：正常数据取一步
		dy=HP_This_ExecTime_Tabl[zone_index];
		if (dy>0) dy--; 
//printf("\r\n 大步正常数据取一步dy=:%d",(int)(dy));
		HP_This_ExecTime_Tabl[zone_index]=dy;

		if (dy==0)//执行时间到时
		{
			HP_Split_TimeDiff_Flag=1;//MARK2017_03_01

//MODI2017_03_14A1
//printf("\r\n HP大步执行时间到=:%d",(int)(1));

//printf("\r\n dddddd---HP执行时间到时:%d",(int)(1));//MODI2016_12_20A3
//printf("\r\n dddddd---HP测试当group=:%d",(int)(group));//MODI2016_12_20A3
//printf("\r\n dddddd---HP测试当block=:%d",(int)(block));//MODI2016_12_20A3

			Refresh_dmx512=1;
			//--------------------------------------
			//计算出当前所有组块的数据，步的数据，与拆分，时差相结合；
			//执行时间到时再运行；
			HP_Execute_This_Step_To_Buf(group,HP_playbuf);
			//--------------------------------------
		}
		
		if (Refresh_dmx512==1)
		{
			Refresh_dmx512=0;
			HP_NewData_Reflash=TRUE;   //手动编程刷新 为1要刷新 0不用；
		}
	}*/
}
