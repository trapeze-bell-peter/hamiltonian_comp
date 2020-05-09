require 'benchmark'
require './hamiltonian'

usa = {
  :wa => [ :or, :id],
  :or => [ :wa, :id, :nv, :ca],
  :ca => [ :or, :nv, :az ],
  :id => [ :wa, :or, :nv, :ut, :wy, :mt ],
  :nv => [ :or, :ca, :az, :ut, :id],
  :ut => [ :id, :nv, :az, :co, :wy ],
  :az => [ :ca, :nv, :ut, :nm ],
  :mt => [ :id, :wy, :sd, :nd ],
  :wy => [ :mt, :id, :ut, :co, :ne, :sd ],
  :co => [ :wy, :ut, :nm, :ok, :ks, :ne ],
  :nm => [ :co, :az, :tx, :ok ],
  :nd => [ :mt, :sd, :mn ],
  :sd => [ :nd, :mt, :wy, :ne, :ia, :mn],
  :ne => [ :sd, :wy, :co, :ks, :mo, :ia],
  :ks => [ :ne, :co, :ok, :mo ],
  :ok => [ :ks, :co, :nm, :tx, :ar, :mo ],
  :tx => [ :ok, :nm, :la, :ar ],
  :mn => [ :nd, :sd, :ia, :wi ],
  :ia => [ :mn, :sd, :ne, :mo, :il, :wi ],
  :mo => [ :ia, :ne, :ks, :ok, :ar, :tn, :ky, :il ],
  :ar => [ :mo, :ok, :tx, :la, :ms, :tn ],
  :la => [ :ar, :tx, :ms ],
  :wi => [ :mn, :ia, :il ],
  :il => [ :wi, :ia, :mo, :ky, :in ],
  :tn => [ :ky, :mo, :ar, :ms, :al, :ga, :nc, :va, :ky ],
  :ms => [ :tn, :ar, :la, :al, :tn ],
  :mi => [ :in, :oh ],
  :in => [ :mi, :il, :ky, :oh ],
  :ky => [ :oh, :in, :il, :mo, :tn, :va, :wv, :oh ],
  :al => [ :tn, :ms, :fl, :ga ],
  :ga => [ :nc, :tn, :al, :fl, :sc ],
  :oh => [ :mi, :in, :ky, :wv, :pa ],
  :wv => [ :pa, :oh, :ky, :va, :md ],
  :ny => [ :pa, :nj, :ct, :ma, :vt ],
  :nj => [ :ny, :pa, :de ],
  :pa => [ :ny, :nj, :oh, :wv, :md, :de ],
  :va => [ :md, :wv, :ky, :tn, :nc, :wdc ],
  :nc => [ :va, :tn, :ga, :sc ],
  :sc => [ :nc, :ga ],
  :fl => [ :ga, :al ],
  :me => [ :nh ],
  :nh => [ :me, :vt, :ma ],
  :vt => [ :nh, :ny, :ma ],
  :ma => [ :nh, :vt, :ny, :ct, :ri ],
  :ct => [ :ma, :ny, :ri ],
  :ri => [ :ma, :ct ],
  :de => [ :pa, :md, :nj ],
  :md => [ :pa, :wv, :va, :de, :wdc ],
  :wdc => [ :md, :va ]
}

eastern_half = [ :mn, :ia, :mo, :ar, :la, :wi, :il, :tn, :ms, :mi, :in, :ky, :al, :ga, :oh, :wv, :ny, :nj, :pa, :va,
               :nc, :sc, :fl, :me, :nh, :vt, :ma, :ct, :ri, :de, :md, :wdc ]

Benchmark.bm do |bm|
  40.times do
    trip1 = Hamiltonian.new( usa, eastern_half )
    bm.report("find hamiltonian"){ trip1.find_hamiltonian( :wdc ) }
  end
end
