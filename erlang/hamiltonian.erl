-module(hamiltonian).
-export([link/2, link/3, reduceGraph/2, findHamiltonian/2, contains/2, mkGraph/1, mkGraph/2, extractNode/2, usa/0, easternStates/0]).

-import(lists, [append/1, usort/1, filter/2, member/2]).

link(Node, Children) -> link([], Node, Children).

link(Original, Node, Children) -> linkNodeRecurse(Original, Node, Children).

linkNodeRecurse([], Node, Children) -> [{Node, Children}];
linkNodeRecurse([ { Node, OldChildren } | R ], Node, NewChildren) -> [ {Node, usort(append([OldChildren, NewChildren]))} | R ] ;
linkNodeRecurse([ Any | R ], Node, Children) -> [Any | linkNodeRecurse(R, Node, Children)].

mkGraph(G) -> mkGraph([], G).
mkGraph(G, []) -> G;
mkGraph(G, [[_ | []] | R]) -> mkGraph(G, R);
mkGraph(G, [[N | Children] | R]) -> mkGraph(link (G, N, Children), R).

retainChildren({ Node, Children}, Selected) -> {Node, filter( fun(Elem) -> member(Elem, Selected) end, Children )} .

contains(_, []) -> false;
contains(E, [E | _]) -> true;
contains(E, [_ | T]) -> contains(E, T).

reduceGraph([], _) -> [];
reduceGraph([{Node, Children} | R], List) ->
		    case member(Node, List) of
		    	 true -> [ retainChildren( { Node, Children }, List ) | reduceGraph(R, List) ] ;
			 false ->  reduceGraph (R, List)
		    end .

extractNode([], _) -> {[], []};
extractNode([ { R , Children } | T ], R) -> { T, Children };
extractNode([ H | T ], R) -> {A, B} = extractNode(T, R), { [ H | A ], B }.


findHamiltonian([], _) -> [];
findHamiltonian(Graph, State) ->
		       { G2 , Children } = extractNode( Graph , State ),
		       findHamiltonianRecurse(G2, {State, Children}, [])
		       .


findHamiltonianRecurse([], {Node, []}, Stack) -> [ Node | Stack ];
findHamiltonianRecurse(_, {_, []}, _) -> [];
findHamiltonianRecurse(Graph, {Node, [ Child | Rest] }, Stack) ->
			      S2 = [ Node | Stack ],
			      { G2 , GrandChildren } = extractNode( Graph , Child ),
			      R = findHamiltonianRecurse( G2, {Child, GrandChildren}, S2),
			      case R of
			      	   [] -> findHamiltonianRecurse( Graph, { Node, Rest }, Stack );
				   Solution -> Solution
			      end .

usa() -> mkGraph([[wa , ore, id],
	[    ore , wa, id, nv, ca],
	[    ca , ore, nv, az],
	[    id , wa, ore, nv, ut, wy, mt],
	[    nv , ore, ca, az, ut, id],
	[    ut , id, nv, az, co, wy],
	[    az , ca, nv, ut, nm],
	[    mt , id, wy, sd, nd],
	[    wy , mt, id, ut, co, ne, sd],
	[    co , wy, ut, nm, ok, ks, ne],
	[    nm , co, az, tx, ok],
	[    nd , mt, sd, mn],
	[    sd , nd, mt, wy, ne, ia, mn],
	[    ne , sd, wy, co, ks, mo, ia],
	[    ks , ne, co, ok, mo],
	[    ok , ks, co, nm, tx, ar, mo],
	[    tx , ok, nm, la, ar],
	[    mn , nd, sd, ia, wi],
	[    ia , mn, sd, ne, mo, il, wi],
	[    mo , ia, ne, ks, ok, ar, tn, ky, il],
	[    ar , mo, ok, tx, la, ms, tn],
	[    la , ar, tx, ms],
	[    wi , mn, ia, il],
	[    il , wi, ia, mo, ky, in],
	[    tn , ky, mo, ar, ms, al, ga, nc, va],
	[    ms , tn, ar, la, al],
	[    mi , in, oh],
	[    in , mi, il, ky, oh],
	[    ky , oh, in, il, mo, tn, va, wv],
	[    al , tn, ms, fl, ga],
	[    ga , nc, tn, al, fl, sc],
	[    oh , mi, in, ky, wv, pa],
	[    wv , pa, oh, ky, va, md],
	[    ny , pa, nj, ct, ma, vt],
	[    nj , ny, pa, de],
	[    pa , ny, nj, oh, wv, md, de],
	[    va , md, wv, ky, tn, nc, wdc],
	[    nc , va, tn, ga, sc],
	[    sc , nc, ga],
	[    fl , ga, al],
	[    me , nh],
	[    nh , me, vt, ma],
	[    vt , nh, ny, ma],
	[    ma , nh, vt, ny, ct, ri],
	[    ct , ma, ny, ri],
	[    ri , ma, ct],
	[    de , pa, md, nj],
	[    md , pa, wv, va, de, wdc],
	[    wdc , md, va]]) .

easternStates() -> [mn, ia, mo, ar, la, wi, il, tn, ms, mi, in, ky, al,
	ga, oh, wv, ny, nj, pa, va, nc, sc, fl, me, nh, vt, ma, ct, ri,
	de, md, wdc].