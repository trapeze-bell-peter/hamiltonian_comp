// Hamiltonian.cpp : Defines the entry point for the console application.
//

#ifdef WINDOWS
#include "stdafx.h"
#endif

#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include <iostream>
#include <ctime>
#include <chrono>

class State
{
public:
	State()
	{
		Children_Count = 0;
		journeyPos = 0;
	}
	~State()
	{
	}
	char Key[10];
	int Children_Count;
	State* Children[10];
	int journeyPos;
};


// Global list of States
State gStates[49];
int gVisitedCount;
int gStateCount;
bool gFilterStates;
char gFilter[][4] = { "mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al", "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri", "de", "md", "wdc" };

bool FilterState(const char* Key)
{
	for (int loop = 0; loop < sizeof(gFilter) / sizeof(gFilter[0]); loop++)
	{
		if (strcmp(Key, gFilter[loop]) == 0)
		{
			return true;
		}
	}
	return false;
}

State* AddNode(const char* Key, const int Child_Count ...)
{
	if (gFilterStates && !FilterState(Key))
	{
		return NULL;
	}
	State* ret_val = NULL;

	// Check if this is an existing state
	for (int state_counter = 0; state_counter < gStateCount; ++state_counter)
	{
		if (strcmp(gStates[state_counter].Key, Key) == 0)
		{
			ret_val = &gStates[state_counter];
			break;
		}
	}


	if (!ret_val)
	{
		strcpy(gStates[gStateCount].Key,Key);
		ret_val = &gStates[gStateCount];
		gStateCount++;
		ret_val->Children_Count = 0;
		ret_val->journeyPos = 0;
	}

	if (Child_Count > 0)
	{
		va_list child_list;
		va_start(child_list, Child_Count);

		ret_val->Children_Count = 0;
		for (int i = 0; i < Child_Count; i++)
		{
			char* Child_Key = va_arg(child_list, char*);
			ret_val->Children[ret_val->Children_Count] = AddNode(Child_Key, 0);
			// Will be null if filtered.
			if (ret_val->Children[ret_val->Children_Count] != NULL)
			{
				++ret_val->Children_Count;
			}
		}
		va_end(child_list);
	}
	return ret_val;
}

void Load_US_States()
{
	gStateCount = 0;
	AddNode("wa", 2, "or", "id");
	AddNode("or", 4, "wa", "id", "nv", "ca");
	AddNode("ca", 3, "or", "nv", "az");
	AddNode("id", 6, "wa", "or", "nv", "ut", "wy", "mt");
	AddNode("nv", 5, "or", "ca", "az", "ut", "id");
	AddNode("ut", 5, "id", "nv", "az", "co", "wy");
	AddNode("az", 4, "ca", "nv", "ut", "nm");
	AddNode("mt", 4, "id", "wy", "sd", "nd");
	AddNode("wy", 6, "mt", "id", "ut", "co", "ne", "sd");
	AddNode("co", 6, "wy", "ut", "nm", "ok", "ks", "ne");
	AddNode("nm", 4, "co", "az", "tx", "ok");
	AddNode("nd", 3, "mt", "sd", "mn");
	AddNode("sd", 6, "nd", "mt", "wy", "ne", "ia", "mn");
	AddNode("ne", 6, "sd", "wy", "co", "ks", "mo", "ia");
	AddNode("ks", 4, "ne", "co", "ok", "mo");
	AddNode("ok", 6, "ks", "co", "nm", "tx", "ar", "mo");
	AddNode("tx", 4, "ok", "nm", "la", "ar");
	AddNode("mn", 4, "nd", "sd", "ia", "wi");
	AddNode("ia", 6, "mn", "sd", "ne", "mo", "il", "wi");
	AddNode("mo", 8, "ia", "ne", "ks", "ok", "ar", "tn", "ky", "il");
	AddNode("ar", 6, "mo", "ok", "tx", "la", "ms", "tn");
	AddNode("la", 3, "ar", "tx", "ms");
	AddNode("wi", 3, "mn", "ia", "il");
	AddNode("il", 5, "wi", "ia", "mo", "ky", "in");
	AddNode("tn", 8, "ky", "mo", "ar", "ms", "al", "ga", "nc", "va");
	AddNode("ms", 4, "tn", "ar", "la", "al");
	AddNode("mi", 2, "in", "oh");
	AddNode("in", 4, "mi", "il", "ky", "oh");
	AddNode("ky", 7, "oh", "in", "il", "mo", "tn", "va", "wv");
	AddNode("al", 4, "tn", "ms", "fl", "ga");
	AddNode("ga", 5, "nc", "tn", "al", "fl", "sc");
	AddNode("oh", 5, "mi", "in", "ky", "wv", "pa");
	AddNode("wv", 5, "pa", "oh", "ky", "va", "md");
	AddNode("ny", 5, "pa", "nj", "ct", "ma", "vt");
	AddNode("nj", 3, "ny", "pa", "de");
	AddNode("pa", 6, "ny", "nj", "oh", "wv", "md", "de");
	AddNode("va", 6, "md", "wv", "ky", "tn", "nc", "wdc");
	AddNode("nc", 4, "va", "tn", "ga", "sc");
	AddNode("sc", 2, "nc", "ga");
	AddNode("fl", 2, "ga", "al");
	AddNode("me", 1, "nh");
	AddNode("nh", 3, "me", "vt", "ma");
	AddNode("vt", 3, "nh", "ny", "ma");
	AddNode("ma", 5, "nh", "vt", "ny", "ct", "ri");
	AddNode("ct", 3, "ma", "ny", "ri");
	AddNode("ri", 2, "ma", "ct");
	AddNode("de", 3, "pa", "md", "nj");
	AddNode("md", 5, "pa", "wv", "va", "de", "wdc");
	AddNode("wdc", 2, "md", "va");


	/* UNUSED - PRINT NETWORK FOR DIAGNOSTICS
	for (State* i = gStates; i != NULL; i = i->pNext)
		{
			std::cout << i->Key << "[";
			for (int j = 0; j < i->Children_Count; j++)
			{
				std::cout << i->Children[j]->Key;
				std::cout << "|";
			}
			std::cout << "]\r\n";
		}
		std::cout << "Press [Enter] to exit";
		std::cin.ignore();*/
}

bool find_hamiltonian_recursively(State* const pStart)
{
	// Add current node to the journey
	pStart->journeyPos = ++gVisitedCount;

	// Have we visited all states?
	if (gVisitedCount == gStateCount)
	{
		std::cout << "Found Path: ";
		for (int i = 1; i < gVisitedCount + 1; ++i)
		{
			for (int j = 0; j < gVisitedCount; ++j)
			{
				if (gStates[j].journeyPos == i)
				{
					std::cout << gStates[j].Key << ",";
				}
			}
		}
		return true;
	}
	else
	{
		const int children_count = pStart->Children_Count;
		for (int neighbour = 0; neighbour < children_count; ++neighbour)
		{
			if (pStart->Children[neighbour]->journeyPos==0)
			{
				if (find_hamiltonian_recursively(pStart->Children[neighbour]))
				{
					return true;
				}
			}
		}
	}

	// Return current to the unvisited list and remove from the journey, and return to allow another route to be explored.
	pStart->journeyPos = 0;
	--gVisitedCount;

	return false;
}

void find_hamiltonian(State* const pStart)
{
	gVisitedCount = 0;

	find_hamiltonian_recursively(pStart);
}

int main(int argc, char* argv[])
{
	// Set initial state from first parameter (default "wdc")
	char initial_state[4]{ "wdc" };
	gFilterStates = true;
	if (argc > 1)
	{
		strcpy(initial_state, argv[1]);
		if (argc > 2)
		{
			if (strcmp(argv[2], "full") == 0)
			{
				gFilterStates = false;
			}
		}
	}

	for (int i = 0; i < 5; i++)
	{
		Load_US_States();
		State* start_state = NULL;
		for (int i = 0; i< gStateCount; ++i)
		{
			if (strcmp(gStates[i].Key, initial_state) == 0)
			{
				start_state = &gStates[i];
				break;
			}
		}
		// Don't run the process if our state doesn't exist
		if (start_state)
		{

			auto begin = std::chrono::high_resolution_clock::now();

			find_hamiltonian(start_state);

			auto end = std::chrono::high_resolution_clock::now();

			std::cout << "(" << std::chrono::duration_cast<std::chrono::nanoseconds>(end - begin).count() / 1000000 << "ms)" << std::endl;
		}
		else
		{
			std::cout << "State doesn't exist: \"" << initial_state << "\" . Use option 'full' to use all states\r\n";
		}
	}

	return 0;
}
