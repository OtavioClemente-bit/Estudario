# O Folha no Blender: pega o modelo final do Meshy (design/mascote/folha-personagem.glb), troca o esqueleto
# automático (cujos pesos prendem a capa do livro aos braços) por um esqueleto feito para ele, anima cada
# humor com keyframes e exporta um GLB leve com todas as animações -> web/src/jsMain/resources/folha.glb.
# O livro, os óculos, os olhos, a luva, o capelo e os tênis ficam como vieram. O rig refaz só o que a malha
# do Meshy não deixa animar direito:
#  - mangas: a do Meshy é um tubo curto de poucos anéis grudado na lateral do livro (deforma feio). Ela sai e
#    entra uma manga remodelada (muitos anéis, junta de ombro), no azul da original, que encaixa na luva;
#  - boca: a original é pintada sobre triângulos grandes (não fecha). A pintura dela sai da textura da página
#    (repintada com o papel em volta), o relevo é assentado na página, e entra uma camada de boca com uma
#    tira de quadros desenhados nas cores da original: sorriso, sorrisão, "hum" e triste;
#  - pálpebras: cascas da cor da página na mesma esfera do olho, que deslizam por cima dele (piscar,
#    feliz, triste, piscadela).
#
#   blender -b --factory-startup --python scripts/web/mascote/folha-blender.py -- design/mascote/folha-personagem.glb web/src/jsMain/resources/folha.glb [arquivo.blend]
#
# Medidas em coordenadas do glTF (y para cima, z para a frente, 1,70 de altura); G() converte para o Blender.
import bpy, bmesh, sys, math
import numpy as np
from mathutils import Vector, Quaternion

argv = sys.argv[sys.argv.index("--") + 1:]
SRC, OUT = argv[0], argv[1]
BLEND = argv[2] if len(argv) > 2 else None
FPS = 30
TAU = 2 * math.pi

def G(x, y, z):
    return Vector((x, -z, y))

def smooth(a, b, x):
    t = np.clip((x - a) / (b - a), 0, 1)
    return t * t * (3 - 2 * t)

# ------------------------------------------------------------------ medidas
EYE = dict(rx=0.1, ry=0.083, h=0.05)
EYE_C = {"L": (-0.21, 0.985), "R": (0.21, 0.983)}
SPH = (EYE["rx"] ** 2 + EYE["h"] ** 2) / (2 * EYE["h"])           # raio da esfera do olho
ROOT = {"L": (-0.625, 0.865, -0.015), "R": (0.625, 0.865, -0.015)}   # ombro: onde a manga sai da lateral do livro
WR = {"L": (-0.795, 0.705, 0.075), "R": (0.795, 0.705, 0.075)}       # pulso: pulseira dourada
ELB = {s: tuple((a + b) / 2 for a, b in zip(ROOT[s], WR[s])) for s in "LR"}
FT = {"L": (-0.17, 0.36, 0.04), "R": (0.205, 0.36, 0.05)}            # tornozelo
MOUTH_AREA = dict(cx=-0.012, cy=0.836, rx=0.15, ry=0.076)            # onde a boca pintada é apagada
DECAL = dict(x0=-0.18, x1=0.15, y0=0.752, y1=0.912)                  # a camada da boca nova

def arm_param(px, py, pz, s):
    """(t, distância) do ponto ao eixo ombro->pulso (t = 0 no ombro, 1 no pulso)."""
    a, w = np.array(ROOT[s]), np.array(WR[s]); d = w - a; L = np.linalg.norm(d); u = d / L
    p = np.stack([px, py, pz], -1) - a; t = p @ u
    perp = np.linalg.norm(p - t[..., None] * u, axis=-1)
    return t / L, perp

# ------------------------------------------------------------------ importa e limpa
bpy.ops.wm.read_factory_settings(use_empty=True)
bpy.ops.import_scene.gltf(filepath=SRC)
scene = bpy.context.scene
scene.render.fps = FPS
mesh_obj = next(o for o in scene.objects if o.type == "MESH")
mw = mesh_obj.matrix_world.copy()
for o in list(scene.objects):
    if o.type != "MESH":
        bpy.data.objects.remove(o, do_unlink=True)
mesh_obj.parent = None
mesh_obj.matrix_world = mw
mesh_obj.modifiers.clear()
mesh_obj.vertex_groups.clear()
bpy.context.view_layer.objects.active = mesh_obj
mesh_obj.select_set(True)
bpy.ops.object.transform_apply(location=True, rotation=True, scale=True)
mesh_obj.name = "Folha"
me = mesh_obj.data

# ------------------------------------------------------------------ dados da malha (no referencial do glTF)
n = len(me.vertices)
co = np.empty(n * 3, np.float32); me.vertices.foreach_get("co", co); co = co.reshape(n, 3)
X, Y, Z = co[:, 0].copy(), co[:, 2].copy(), -co[:, 1].copy()
nrm = np.empty(n * 3, np.float32); me.vertices.foreach_get("normal", nrm); NXv = nrm.reshape(n, 3)[:, 0]
me.calc_loop_triangles()
T = len(me.loop_triangles)
tv = np.empty(T * 3, np.int32); me.loop_triangles.foreach_get("vertices", tv); tv = tv.reshape(T, 3)
tl = np.empty(T * 3, np.int32); me.loop_triangles.foreach_get("loops", tl); tl = tl.reshape(T, 3)
nl = len(me.loops)
luv = np.empty(nl * 2, np.float32); me.uv_layers.active.data.foreach_get("uv", luv); luv = luv.reshape(nl, 2)
lvi = np.empty(nl, np.int32); me.loops.foreach_get("vertex_index", lvi)
vuv = np.zeros((n, 2), np.float32); vuv[lvi] = luv

# Textura de cor (reduzida, só para ler as cores).
mat = mesh_obj.active_material
bsdf = next(nd for nd in mat.node_tree.nodes if nd.type == "BSDF_PRINCIPLED")
def image_into(socket):
    if not socket.is_linked: return None
    nd = socket.links[0].from_node
    while nd.type != "TEX_IMAGE" and nd.inputs and any(i.is_linked for i in nd.inputs):
        nd = next(i for i in nd.inputs if i.is_linked).links[0].from_node
    return nd.image if nd.type == "TEX_IMAGE" else None
base_img = image_into(bsdf.inputs["Base Color"])
small = base_img.copy(); small.scale(512, 512)
px = np.empty(512 * 512 * 4, np.float32); small.pixels.foreach_get(px); px = (px.reshape(512, 512, 4)[:, :, :3] * 255)
bpy.data.images.remove(small)
def color_uv(uv):
    u = np.clip((uv[..., 0] * 512).astype(int), 0, 511); v = np.clip((uv[..., 1] * 512).astype(int), 0, 511)
    return px[v, u]
VC = color_uv(vuv)
R, Gc, Bc = VC[:, 0], VC[:, 1], VC[:, 2]
LUM = 0.3 * R + 0.59 * Gc + 0.11 * Bc
def is_paper(c): return (0.3 * c[..., 0] + 0.59 * c[..., 1] + 0.11 * c[..., 2] > 180) & (c[..., 0] - c[..., 2] > 4) & (c[..., 0] - c[..., 2] < 55) & (c[..., 1] >= c[..., 0] - 25)
PAPER = is_paper(VC)
NAVY = (Bc > R + 25) & (LUM < 110)
GLOVE = (LUM > 150) | ((R > 120) & (R - Bc > 60))
GOLD = (R > 110) & (R - Bc > 55) & (Gc > 0.55 * R)      # dourado tem bastante verde; o vermelho da boca, não
RED = (R > 140) & (Gc < 0.6 * R)

# ------------------------------------------------------------------ altura da página (rasteriza os triângulos de papel virados para a frente)
X0, Y0, RES, NX, NY = -0.66, 0.52, 0.02, 67, 48
Hm = np.full((NY, NX), -9.0)
TCOL = color_uv(luv[tl].mean(axis=1))
paper_t = is_paper(TCOL)
for t in range(T):
    if not paper_t[t]: continue
    a, b, c = tv[t]
    ax, ay, az, bx, by, bz, cx, cy, cz = X[a], Y[a], Z[a], X[b], Y[b], Z[b], X[c], Y[c], Z[c]
    if (bx - ax) * (cy - ay) - (by - ay) * (cx - ax) <= 0 or max(az, bz, cz) < 0: continue
    den = (by - cy) * (ax - cx) + (cx - bx) * (ay - cy)
    if abs(den) < 1e-9: continue
    i0 = max(0, math.ceil((min(ax, bx, cx) - X0) / RES)); i1 = min(NX - 1, math.floor((max(ax, bx, cx) - X0) / RES))
    j0 = max(0, math.ceil((min(ay, by, cy) - Y0) / RES)); j1 = min(NY - 1, math.floor((max(ay, by, cy) - Y0) / RES))
    for j in range(j0, j1 + 1):
        for i in range(i0, i1 + 1):
            x, y = X0 + i * RES, Y0 + j * RES
            w1 = ((by - cy) * (x - cx) + (cx - bx) * (y - cy)) / den; w2 = ((cy - ay) * (x - cx) + (ax - cx) * (y - cy)) / den; w3 = 1 - w1 - w2
            if w1 < -1e-4 or w2 < -1e-4 or w3 < -1e-4: continue
            Hm[j, i] = max(Hm[j, i], w1 * az + w2 * bz + w3 * cz)
# sob a boca a página é lisa: tira o relevo do queixo/lábios da altura e preenche pelos lados
_gx = X0 + np.arange(NX) * RES; _gy = Y0 + np.arange(NY) * RES; _GX, _GY = np.meshgrid(_gx, _gy)
Hm[((_GX + 0.012) / 0.19) ** 2 + ((_GY - 0.836) / 0.1) ** 2 < 1] = -9
for _ in range(14):
    m = Hm > -9; acc = np.zeros_like(Hm); cnt = np.zeros_like(Hm)
    for dj, di in ((1, 0), (-1, 0), (0, 1), (0, -1)):
        acc += np.roll(np.roll(np.where(m, Hm, 0), dj, 0), di, 1); cnt += np.roll(np.roll(m, dj, 0), di, 1)
    fill = (~m) & (cnt > 0); Hm[fill] = acc[fill] / cnt[fill]
def page_z(x, y):
    fx = np.clip((np.asarray(x) - X0) / RES, 0, NX - 1.001); fy = np.clip((np.asarray(y) - Y0) / RES, 0, NY - 1.001)
    ix, iy = fx.astype(int), fy.astype(int); tx, ty = fx - ix, fy - iy
    return (Hm[iy, ix] * (1 - tx) + Hm[iy, ix + 1] * tx) * (1 - ty) + (Hm[iy + 1, ix] * (1 - tx) + Hm[iy + 1, ix + 1] * tx) * ty
PZ = page_z(X, Y)
pz = lambda x, y: float(page_z(x, y))
EYE_CTR = {s: (EYE_C[s][0], EYE_C[s][1], pz(*EYE_C[s]) + EYE["h"] - SPH) for s in "LR"}   # centro da esfera do olho

# ------------------------------------------------------------------ boca original: medidas, apagar a pintura e assentar o relevo
mred = RED & (np.abs(X + 0.01) < 0.16) & (Y > 0.74) & (Y < 0.92) & (Z > PZ - 0.15)
MOUTH_RED = np.median(VC[mred], axis=0) / 255 if mred.any() else np.array([0.85, 0.25, 0.27])
mm_ = mred | ((LUM > 200) & (np.abs(X + 0.01) < 0.14) & (Y > 0.80) & (Y < 0.895) & (Z < PZ - 0.004))   # vermelho e dentes
SMILE = dict(x0=float(X[mred].min()), x1=float(X[mred].max()), top=float(Y[mm_].max()), bot=float(Y[mred].min()))
print("boca original:", {k: round(v, 3) for k, v in SMILE.items()}, "vermelho", (MOUTH_RED * 255).astype(int))
in_area = (((X - MOUTH_AREA["cx"]) / (MOUTH_AREA["rx"] + 0.02)) ** 2 + ((Y - MOUTH_AREA["cy"]) / (MOUTH_AREA["ry"] + 0.015)) ** 2 < 1)
# (tudo o que estiver perto da boca e saltar da página, menos o aro dos óculos, que passa nos cantos de cima)
glasses_rim = (GOLD & (Y > 0.86)) | ((LUM < 60) & (np.abs(X - MOUTH_AREA["cx"]) > 0.1) & (Y > 0.86))
flat_v = (((X - MOUTH_AREA["cx"]) / 0.2) ** 2 + ((Y - MOUTH_AREA["cy"]) / 0.105) ** 2 < 1) & (Z > PZ - 0.15) & (np.abs(Z - PZ) > 0.0005) & ~glasses_rim
# 1) repinta a textura da página onde a boca estava (cada pixel vira o papel em volta, por difusão)
BW_, BH_ = base_img.size
full = np.empty(BW_ * BH_ * 4, np.float32); base_img.pixels.foreach_get(full); full = full.reshape(BH_, BW_, 4)
foot = np.zeros((BH_, BW_), bool); hole = np.zeros((BH_, BW_), bool)
tcol_m = color_uv(luv[tl].mean(axis=1))
tri_gold = (tcol_m[:, 0] > 110) & (tcol_m[:, 0] - tcol_m[:, 2] > 55) & (tcol_m[:, 1] > 0.55 * tcol_m[:, 0])
tri_m = (in_area[tv].any(1)) & (Z[tv].mean(1) > PZ[tv].mean(1) - 0.15) & ~tri_gold & ~(Y[tv].mean(1) > 0.875) | (in_area[tv].all(1) & ~tri_gold)
for t in np.nonzero(tri_m)[0]:
    P3 = np.stack([X[tv[t]], Y[tv[t]]], 1); U = luv[tl[t]] * [BW_, BH_] - 0.5
    den = (U[1, 1] - U[2, 1]) * (U[0, 0] - U[2, 0]) + (U[2, 0] - U[1, 0]) * (U[0, 1] - U[2, 1])
    if abs(den) < 1e-6: continue
    i0, i1 = max(0, int(U[:, 0].min()) - 1), min(BW_ - 1, int(U[:, 0].max()) + 2)
    j0, j1 = max(0, int(U[:, 1].min()) - 1), min(BH_ - 1, int(U[:, 1].max()) + 2)
    gx, gy = np.meshgrid(np.arange(i0, i1 + 1), np.arange(j0, j1 + 1))
    w1 = ((U[1, 1] - U[2, 1]) * (gx - U[2, 0]) + (U[2, 0] - U[1, 0]) * (gy - U[2, 1])) / den
    w2 = ((U[2, 1] - U[0, 1]) * (gx - U[2, 0]) + (U[0, 0] - U[2, 0]) * (gy - U[2, 1])) / den
    w3 = 1 - w1 - w2
    ins = (w1 >= -0.05) & (w2 >= -0.05) & (w3 >= -0.05)
    x3 = w1 * P3[0, 0] + w2 * P3[1, 0] + w3 * P3[2, 0]; y3 = w1 * P3[0, 1] + w2 * P3[1, 1] + w3 * P3[2, 1]
    inner = ins & ((((x3 - MOUTH_AREA["cx"]) / MOUTH_AREA["rx"]) ** 2 + ((y3 - MOUTH_AREA["cy"]) / MOUTH_AREA["ry"]) ** 2) < 1)
    foot[j0:j1 + 1, i0:i1 + 1] |= ins; hole[j0:j1 + 1, i0:i1 + 1] |= inner
ys, xs = np.nonzero(foot)
if len(ys):
    j0, j1, i0, i1 = ys.min(), ys.max() + 1, xs.min(), xs.max() + 1
    blk = full[j0:j1, i0:i1, :3]; hb = hole[j0:j1, i0:i1]; fb = foot[j0:j1, i0:i1]
    # some tudo dentro da área da boca (a pintura, o sombreado do relevo, o vinco do queixo) e qualquer
    # vermelho que tenha ficado nos triângulos dela
    c255 = blk * 255
    hb |= fb & (c255[..., 0] > 120) & (c255[..., 1] < 0.72 * c255[..., 0]) & (c255[..., 0] - c255[..., 2] > 40)
    for _ in range(2): hb = hb | (np.roll(hb, 1, 0) | np.roll(hb, -1, 0) | np.roll(hb, 1, 1) | np.roll(hb, -1, 1)) & fb
    known = fb & ~hb
    pcol = np.median(blk[known], axis=0) if known.any() else np.array([0.95, 0.92, 0.85])
    # preenche do papel conhecido para dentro: média borrada (só de pixels do mesmo pedaço de página),
    # do raio pequeno ao grande, e uma suavização final
    def box(a, r):
        for ax in (0, 1):
            c = np.cumsum(np.pad(a, [(r + 1, r) if i == ax else (0, 0) for i in range(a.ndim)], mode="edge"), axis=ax)
            a = (np.take(c, range(2 * r + 1, c.shape[ax]), axis=ax) - np.take(c, range(0, c.shape[ax] - 2 * r - 1), axis=ax)) / (2 * r + 1)
        return a
    K = known.astype(np.float32); todo = hb.copy()
    for rad in (4, 12, 32, 90, 240):
        den = box(K, rad); num = box(blk * K[..., None], rad)
        ok = todo & (den > 0.02)
        blk[ok] = num[ok] / den[ok][:, None]; todo &= ~ok
    blk[todo] = pcol
    for _ in range(40):
        avg = (np.roll(blk, 1, 0) + np.roll(blk, -1, 0) + np.roll(blk, 1, 1) + np.roll(blk, -1, 1)) / 4
        blk[hb] = avg[hb]
    full[j0:j1, i0:i1, :3] = blk
    base_img.pixels.foreach_set(full.ravel())
    print("boca: pixels repintados", int(hb.sum()))
del full
# 2) o relevo da boca (cavidade, dentes, lábios) assenta na página
for v in np.nonzero(flat_v)[0]:
    me.vertices[int(v)].co = G(X[v], Y[v], PZ[v] - 0.001)
Z[flat_v] = PZ[flat_v] - 0.001
print("boca: vértices assentados", int(flat_v.sum()))
_near = (((X - MOUTH_AREA["cx"]) / MOUTH_AREA["rx"]) ** 2 + ((Y - MOUTH_AREA["cy"]) / MOUTH_AREA["ry"]) ** 2 < 1)
_hi = _near & (Z > PZ + 0.004)
print("boca: ainda acima da página", int(_hi.sum()), [(round(float(X[i]), 3), round(float(Y[i]), 3), round(float(Z[i] - PZ[i]), 3), tuple(int(c) for c in VC[i])) for i in np.nonzero(_hi)[0][:12]])

# ------------------------------------------------------------------ esqueleto
BONES = [
    # nome, cabeça, cauda, pai
    ("root", (0, 0, 0), (0, 0.2, 0), None),
    ("body", (0, 0.52, 0), (0, 1.3, 0), "root"),
    ("cap", (0.02, 1.45, -0.05), (0.02, 1.62, -0.05), "body"),
    ("tassel", (0.31, 1.61, 0.16), (0.42, 1.15, 0.27), "cap"),
    ("ribbon", (0.03, 0.6, 0.17), (0.03, 0.45, 0.18), "body"),
    ("glasses", (0, 1.0, 0.3), (0, 1.1, 0.3), "body"),
    # boca: o osso fica no lábio de cima (a camada da boca abre para baixo quando ele escala em y)
    ("mouth", (SMILE_CX := (SMILE["x0"] + SMILE["x1"]) / 2, SMILE["top"], pz(-0.01, 0.85)), (SMILE_CX, SMILE["top"] - 0.1, pz(-0.01, 0.85)), "body"),
]
for s in "LR":
    ex, ey, ez = EYE_CTR[s]
    BONES += [
        ("eye." + s, (ex, ey, ez), (ex, ey + 0.1, ez), "body"),
        ("lidU." + s, (ex, ey, ez), (ex, ey + 0.08, ez), "body"),
        ("lidD." + s, (ex, ey, ez), (ex, ey + 0.08, ez), "body"),
        ("brow." + s, (ex, 1.2, pz(ex, 1.2)), (ex, 1.28, pz(ex, 1.2)), "body"),
        ("arm." + s, ROOT[s], ELB[s], "body"),
        ("fore." + s, ELB[s], WR[s], "arm." + s),
        ("hand." + s, WR[s], (WR[s][0] * 1.08, 0.56, 0.13), "fore." + s),
        ("foot." + s, FT[s], (FT[s][0], 0.22, 0.22), "root"),
    ]
arm_data = bpy.data.armatures.new("FolhaRig")
arm = bpy.data.objects.new("FolhaRig", arm_data)
scene.collection.objects.link(arm)
bpy.context.view_layer.objects.active = arm
for o in scene.objects: o.select_set(o is arm)
bpy.ops.object.mode_set(mode="EDIT")
for name, h, t, parent in BONES:
    eb = arm_data.edit_bones.new(name); eb.head = G(*h); eb.tail = G(*t); eb.roll = 0
    if parent: eb.parent = arm_data.edit_bones[parent]; eb.use_connect = False
bpy.ops.object.mode_set(mode="OBJECT")

# ------------------------------------------------------------------ pesos
W = {name: np.zeros(n, np.float32) for name, *_ in BONES}
done = np.zeros(n, bool)
side = np.where(X < 0.02, "L", "R")
AX = np.abs(X)
front = Z > PZ + 0.006
def put(mask, bone):
    global done
    m = mask & ~done; W[bone][m] += 1; done |= m
# fita marcadora do meio
rib = (np.abs(X - 0.03) < 0.075) & (Y > 0.43) & (Y < 0.6) & (Z > 0.05)
t = smooth(0.6, 0.54, Y); m = rib & ~done; W["ribbon"][m] += t[m]; W["body"][m] += 1 - t[m]; done |= m
# pernas e tênis: o pé manda embaixo; a perna estica até o livro
legs = (Y < 0.53) & (AX < 0.55) & ~done
t = smooth(0.35, 0.5, Y)
for s in "LR":
    m = legs & (side == s); W["foot." + s][m] += 1 - t[m]; W["body"][m] += t[m]
done |= legs
# braços: a luva (branca e dourada, perto do pulso) vai com a mão; a manga do Meshy sai da malha
SLEEVE = np.zeros(n, bool)
for s in "LR":
    tt, perp = arm_param(X, Y, Z, s)
    ms = (side == s) & ~done
    dw = np.sqrt((X - WR[s][0]) ** 2 + (Y - WR[s][1]) ** 2 + (Z - WR[s][2]) ** 2)
    glove = ms & GLOVE & (AX > 0.69) & (Y < 0.78) & (Y > 0.42) & (dw < 0.32) & (tt > 0.6)
    near = ms & (AX > 0.69) & (Y > 0.42) & (Y < 0.95) & ~glove & (dw < 0.32) & (tt > 0.75)   # partes sombreadas da luva
    W["hand." + s][glove | near] = 1
    sleeve = ms & ~glove & ~near & (AX > 0.6) & (tt > -0.05) & (tt < 1.08) & (perp < 0.085) & (Y > 0.6) & (Y < 0.95) & (Z > -0.1) & (Z < 0.17) & ~((np.abs(NXv) > 0.9) & (AX < 0.648))
    sleeve |= ms & ~glove & ~near & (AX > 0.69) & (Y > 0.42) & (Y < 0.95)
    SLEEVE |= sleeve
    W["body"][sleeve] = 1
    done |= glove | near | sleeve
# borla: a corda dourada que desce do botão do capelo
tas = GOLD & (X > 0.24) & (Y > 1.06) & (Y < 1.62) & (Z > 0.12) & ~done
t = smooth(1.61, 1.5, Y); W["tassel"][tas] += t[tas]; W["cap"][tas] += 1 - t[tas]; done |= tas
put(Y > 1.465, "cap")
# rosto
for s in "LR":
    ex, ey = EYE_C[s]
    put((((X - ex) / EYE["rx"]) ** 2 + ((Y - ey) / EYE["ry"]) ** 2 < 1) & ~GOLD & (Z > PZ - 0.02) & (Z < PZ + 0.09), "eye." + s)
for s in "LR":
    put(front & (side == s) & (AX > 0.03) & (AX < 0.34) & (Y > 1.13) & (Y < 1.28) & NAVY, "brow." + s)
put(front & (Y > 0.865) & (Y < 1.125) & (AX < 0.6) & (GOLD | (LUM < 60)) & (Z > PZ + 0.012), "glasses")
W["body"][~done] = 1
tot = sum(W.values()); tot[tot == 0] = 1
for name, w in W.items():
    w /= tot
    vg = mesh_obj.vertex_groups.new(name=name)
    idx = np.nonzero(w > 1e-4)[0]
    for v in np.unique(np.round(w[idx], 3)):
        sel = idx[np.round(w[idx], 3) == v]
        vg.add(sel.tolist(), float(v), "REPLACE")
mesh_obj.vertex_groups.new(name="_del").add(np.nonzero(SLEEVE)[0].tolist(), 1.0, "REPLACE")
mesh_obj.vertex_groups.new(name="_flat").add(np.nonzero(flat_v)[0].tolist(), 1.0, "REPLACE")
print("pesos:", {k: int((v > 0.5).sum()) for k, v in W.items() if k != "body"}, "manga removida", int(SLEEVE.sum()))

# ------------------------------------------------------------------ peças novas
TCX, TCY = X[tv].mean(1), Y[tv].mean(1)
TZ = Z[tv].mean(1); TPZ = page_z(TCX, TCY)
T_PAPER = is_paper(TCOL) & (TZ > TPZ - 0.01)
T_NAVY = (TCOL[:, 2] > TCOL[:, 0] + 25) & (0.3 * TCOL[:, 0] + 0.59 * TCOL[:, 1] + 0.11 * TCOL[:, 2] < 110)
def pick_uv(tmask, cx, cy):
    """UV do meio de um triângulo (não de um vértice, que pode estar numa costura da textura)."""
    idx = np.nonzero(tmask)[0]
    k = idx[np.argmin((TCX[idx] - cx) ** 2 + (TCY[idx] - cy) ** 2)]
    return tuple(luv[tl[k]].mean(0))

def new_object(name, bm, material, groups):
    me2 = bpy.data.meshes.new(name); bm.to_mesh(me2); bm.free()
    ob = bpy.data.objects.new(name, me2); scene.collection.objects.link(ob)
    me2.materials.append(material)
    for g, ids_w in groups.items():
        vg = ob.vertex_groups.new(name=g)
        for i, w in ids_w: vg.add([i], float(w), "REPLACE")
    return ob

# ---- pálpebras (cascas na esfera do olho, da cor da página, com cílios)
THETA = math.asin(min(0.99, EYE["rx"] * 1.08 / SPH))   # raio angular da casca (cobre o olho com folga)
OPEN = 2 * THETA + 0.15                                  # quanto ela gira para sumir dentro da página
RL = SPH + 0.009
def make_lid(s, lower):
    ex, ey, ez = EYE_CTR[s]
    cand = T_PAPER & (np.abs(TCX - ex) < 0.14) & (TCY > 1.08) & (TCY < 1.16)
    uv_paper = pick_uv(cand if cand.any() else T_PAPER, ex, 1.12)
    uv_lash = pick_uv(T_NAVY & (np.abs(TCX - ex) < 0.15) & (TCY > 1.13) & (TCY < 1.28), ex, 1.2)
    bm = bmesh.new(); uvl = bm.loops.layers.uv.new("UVMap")
    C = np.array([ex, ey, ez])
    def sp(a, b, r):   # a: ângulo a partir da frente; b: em volta (0 = para a direita, pi/2 = para cima)
        return G(*(C + r * np.array([math.sin(a) * math.cos(b), math.sin(a) * math.sin(b), math.cos(a)])))
    NA, NB = 8, 40
    tip = bm.verts.new(sp(0, 0, RL)); rings = []
    for i in range(1, NA + 1):
        a = THETA * i / NA
        rings.append([bm.verts.new(sp(a, TAU * j / NB, RL)) for j in range(NB)])
    faces = [bm.faces.new((tip, rings[0][j], rings[0][(j + 1) % NB])) for j in range(NB)]
    for i in range(NA - 1):
        for j in range(NB):
            faces.append(bm.faces.new((rings[i][j], rings[i + 1][j], rings[i + 1][(j + 1) % NB], rings[i][(j + 1) % NB])))
    # cílios: tubinho na borda que vai na frente (a de baixo da pálpebra de cima; a de cima da de baixo)
    lash = []
    b0, b1 = (0.25, math.pi - 0.25) if lower else (math.pi + 0.25, TAU - 0.25)
    NL, NR, rr = 28, 6, 0.0045
    ringsL = []
    for k in range(NL + 1):
        b = b0 + (b1 - b0) * k / NL
        p = np.array(sp(THETA, b, RL + 0.002)); pn = np.array(sp(THETA, b + 1e-3, RL + 0.002)); tg = pn - p; tg /= np.linalg.norm(tg)
        nv = np.array(sp(THETA, b, RL + 0.012)) - p; nv /= np.linalg.norm(nv); bi = np.cross(tg, nv)
        th = 1 - abs(2 * k / NL - 1) ** 3                 # afina nas pontas
        ringsL.append([bm.verts.new(Vector(p + rr * (0.3 + 0.7 * th) * (math.cos(TAU * q / NR) * nv + math.sin(TAU * q / NR) * bi))) for q in range(NR)])
    for k in range(NL):
        for q in range(NR):
            lash.append(bm.faces.new((ringsL[k][q], ringsL[k + 1][q], ringsL[k + 1][(q + 1) % NR], ringsL[k][(q + 1) % NR])))
    bmesh.ops.recalc_face_normals(bm, faces=faces + lash)
    for f in faces:
        for l in f.loops: l[uvl].uv = uv_paper
    for f in lash:
        for l in f.loops: l[uvl].uv = uv_lash
    for f in faces + lash: f.smooth = True
    name = ("lidD." if lower else "lidU.") + s
    nv_ = len(bm.verts)
    return new_object(name, bm, mat, {name: [(i, 1.0) for i in range(nv_)]})

# ---- mangas remodeladas: tubo de muitos anéis do ombro (dentro do livro) até dentro do punho da luva, com
# uma junta de ombro; azul da manga original, sem o mapa de relevo (a cor é lisa)
manga = mat.copy(); manga.name = "manga"
mb_ = next(nd for nd in manga.node_tree.nodes if nd.type == "BSDF_PRINCIPLED")
for l_ in list(mb_.inputs["Normal"].links): manga.node_tree.links.remove(l_)
uv_navy = pick_uv(T_NAVY & (np.abs(np.abs(TCX) - 0.72) < 0.08) & (np.abs(TCY - 0.79) < 0.08), 0.72, 0.79)
print("manga: azul", [int(v) for v in color_uv(np.array(uv_navy))])
def make_sleeve(s):
    a, w = np.array(ROOT[s]), np.array(WR[s]); d = w - a; L = np.linalg.norm(d); u = d / L
    up = np.array([0, 0, 1.0]); n1 = np.cross(u, up); n1 /= np.linalg.norm(n1); n2 = np.cross(n1, u)
    bm = bmesh.new(); uvl = bm.loops.layers.uv.new("UVMap"); groups = {"arm." + s: [], "fore." + s: [], "hand." + s: []}
    NRg, NS = 30, 20
    rings = []
    t0, t1 = -0.12, 1.06                                   # começa dentro do livro, termina dentro do punho
    for i in range(NRg + 1):
        tt = t0 + (t1 - t0) * i / NRg
        c = a + u * (tt * L)
        r = 0.036 + 0.009 * float(smooth(0.18, -0.05, tt))  # um pouco mais larga no ombro
        ring = []
        for j in range(NS):
            ang = TAU * j / NS
            p = c + r * (math.cos(ang) * n1 + math.sin(ang) * n2)
            v = bm.verts.new(G(*p)); ring.append(v)
        rings.append((ring, tt))
    faces = []
    for i in range(NRg):
        for j in range(NS):
            faces.append(bm.faces.new((rings[i][0][j], rings[i][0][(j + 1) % NS], rings[i + 1][0][(j + 1) % NS], rings[i + 1][0][j])))
    # junta do ombro: uma esfera que fecha a lateral do livro onde a manga sai
    sph = bmesh.ops.create_uvsphere(bm, u_segments=20, v_segments=12, radius=0.05)
    for v in sph["verts"]: v.co += G(*a)
    bm.verts.ensure_lookup_table(); bm.faces.ensure_lookup_table()
    for f in bm.faces:
        f.smooth = True
        for l in f.loops: l[uvl].uv = uv_navy
    bmesh.ops.recalc_face_normals(bm, faces=list(bm.faces))
    bm.verts.index_update()
    tring = {}
    for ring, tt in rings:
        for v in ring: tring[v.index] = tt
    for v in bm.verts:
        tt = tring.get(v.index, 0.0)
        wa = 1 - float(smooth(0.35, 0.6, tt)); wh = float(smooth(0.88, 1.0, tt)); wf = max(0.0, 1 - wa - wh)
        for g, wv in (("arm." + s, wa), ("fore." + s, wf), ("hand." + s, wh)):
            if wv > 1e-3: groups[g].append((v.index, wv))
    return new_object("manga." + s, bm, manga, groups)

# ---- boca nova: camada fina sobre a página com uma tira de quadros desenhados
NF = 4                                   # 0 sorriso (como o original), 1 sorrisão, 2 "hum", 3 triste
FWp, FHp, SS = 640, 300, 3               # pixels por quadro e supersampling
def draw_frames():
    W_, H_ = FWp * SS, FHp * SS
    xs = DECAL["x0"] + (np.arange(W_) + 0.5) / W_ * (DECAL["x1"] - DECAL["x0"])
    ys = DECAL["y0"] + (np.arange(H_) + 0.5) / H_ * (DECAL["y1"] - DECAL["y0"])
    GX, GY = np.meshgrid(xs, ys)
    red = MOUTH_RED; dark = red * 0.45; teeth = np.array([0.98, 0.97, 0.95]); tongue = np.array([0.97, 0.55, 0.58])
    cx = (SMILE["x0"] + SMILE["x1"]) / 2; hw0 = (SMILE["x1"] - SMILE["x0"]) / 2; depth0 = SMILE["top"] - SMILE["bot"]
    px_ = (DECAL["x1"] - DECAL["x0"]) / W_
    def erode(m, k):
        for _ in range(k): m = m & np.roll(m, 1, 0) & np.roll(m, -1, 0) & np.roll(m, 1, 1) & np.roll(m, -1, 1)
        return m
    def smile(scale_w, scale_d):
        hw, depth = hw0 * scale_w, depth0 * scale_d
        xn = (GX - cx) / hw
        top = SMILE["top"] - 0.004 * (1 - np.clip(xn, -1, 1) ** 2) + 0.002 * np.clip(xn, -1, 1) ** 4
        bot = top - depth * np.clip(1 - xn ** 2, 0, 1) ** 0.72
        inside = (np.abs(xn) < 1) & (GY < top) & (GY > bot)
        rgb = np.zeros(GX.shape + (3,)); rgb[:] = red
        k = np.clip((top - GY) / depth, 0, 1)[..., None]; rgb = red * (0.72 + 0.28 * k)          # mais escuro no fundo de cima
        tg = ((GX - cx) / (hw * 0.5)) ** 2 + ((GY - (top - depth * 0.9)) / (depth * 0.42)) ** 2 < 1
        rgb[tg] = tongue
        th = (GY > top - min(0.013, depth * 0.2)) & (np.abs(xn) < 0.86)
        rgb[th] = teeth
        edge = inside & ~erode(inside, max(1, int(0.0032 / px_)))
        rgb[edge] = dark
        return rgb, inside.astype(np.float32)
    def line(curve, hw, thick):
        us = np.linspace(-1, 1, 200); lx = cx + us * hw; ly = np.array([curve(q) for q in us])
        d = np.full(GX.shape, 9.0)
        for x_, y_ in zip(lx, ly): d = np.minimum(d, np.hypot(GX - x_, GY - y_))
        m = d < thick / 2
        rgb = np.zeros(GX.shape + (3,)); rgb[:] = red * 0.62
        return rgb, m.astype(np.float32)
    frames = [smile(1.0, 1.0), smile(1.08, 1.14),
              line(lambda q: 0.848 + 0.006 * q - 0.004 * (1 - q * q), 0.05, 0.012),
              line(lambda q: 0.826 + 0.027 * (1 - q * q) ** 0.8, 0.066, 0.0135)]
    # Duas faixas: em cima a cor, embaixo a transparência em cinza (o GLB leva as texturas em JPEG, sem
    # canal alfa; o app usa a faixa de baixo como alphaMap).
    # fundo: uma "pele" de papel opaca com borda macia, que cobre o que sobrar da boca original
    pmask = (((GX - MOUTH_AREA["cx"]) / (MOUTH_AREA["rx"] * 0.98)) ** 2 + ((GY - MOUTH_AREA["cy"]) / (MOUTH_AREA["ry"] * 0.98)) ** 2)
    pa = np.clip((1.0 - pmask) / 0.35, 0, 1) ** 1.5
    cols, alps = [], []
    for rgb, a_ in frames:
        rgb = rgb * a_[..., None] + PAPER_COL * (1 - a_[..., None]); a_ = np.maximum(a_, pa)
        rgba = np.concatenate([rgb * a_[..., None], a_[..., None]], -1)
        rgba = rgba.reshape(FHp, SS, FWp, SS, 4).mean((1, 3))                                    # supersampling
        al = rgba[..., 3:]
        col = np.where(al > 1e-4, rgba[..., :3] / np.maximum(al, 1e-4), dark)
        cols.append(np.concatenate([col, np.ones_like(al)], -1)); alps.append(np.concatenate([np.repeat(al, 3, -1), np.ones_like(al)], -1))
    return np.concatenate([np.concatenate(alps, axis=1), np.concatenate(cols, axis=1)], axis=0)   # linha 0 do Blender = embaixo
near_m = PAPER & (np.abs(X - MOUTH_AREA["cx"]) < 0.24) & (np.abs(Y - MOUTH_AREA["cy"]) < 0.12) & ~in_area
PAPER_COL = np.median(VC[near_m], axis=0) / 255 if near_m.any() else np.array([0.96, 0.93, 0.86])
print("boca: papel", (PAPER_COL * 255).astype(int))
atlas = draw_frames()
boca_img = bpy.data.images.new("boca", FWp * NF, FHp * 2, alpha=False)
boca_img.pixels.foreach_set(atlas.astype(np.float32).ravel())
if BLEND: boca_img.filepath_raw = BLEND + ".boca.png"; boca_img.file_format = "PNG"; boca_img.save()
boca_img.pack()
boca = bpy.data.materials.new("boca"); boca.use_nodes = True
nt = boca.node_tree; pb_ = next(nd for nd in nt.nodes if nd.type == "BSDF_PRINCIPLED")
texn = nt.nodes.new("ShaderNodeTexImage"); texn.image = boca_img; texn.interpolation = "Linear"
nt.links.new(texn.outputs["Color"], pb_.inputs["Base Color"])
pb_.inputs["Roughness"].default_value = 0.92
pb_.inputs["Specular IOR Level"].default_value = 0.2
def make_decal():
    bm = bmesh.new(); uvl = bm.loops.layers.uv.new("UVMap")
    NU, NV = 56, 28
    grid = [[bm.verts.new(G(DECAL["x0"] + (DECAL["x1"] - DECAL["x0"]) * i / NU, DECAL["y0"] + (DECAL["y1"] - DECAL["y0"]) * j / NV,
                            pz(DECAL["x0"] + (DECAL["x1"] - DECAL["x0"]) * i / NU, DECAL["y0"] + (DECAL["y1"] - DECAL["y0"]) * j / NV) + 0.014))
             for i in range(NU + 1)] for j in range(NV + 1)]
    for j in range(NV):
        for i in range(NU):
            f = bm.faces.new((grid[j][i], grid[j][i + 1], grid[j + 1][i + 1], grid[j + 1][i])); f.smooth = True
            for l, (ii, jj) in zip(f.loops, ((i, j), (i + 1, j), (i + 1, j + 1), (i, j + 1))): l[uvl].uv = (ii / NU / NF, 0.5 + 0.5 * jj / NV)
    bmesh.ops.recalc_face_normals(bm, faces=list(bm.faces))
    nv_ = len(bm.verts)
    return new_object("boca", bm, boca, {"mouth": [(i, 1.0) for i in range(nv_)]})

parts = [make_lid(s, lower) for s in "LR" for lower in (False, True)] + [make_sleeve(s) for s in "LR"] + [make_decal()]
bpy.ops.object.select_all(action="DESELECT")
for o in parts: o.select_set(True)
mesh_obj.select_set(True); bpy.context.view_layer.objects.active = mesh_obj
bpy.ops.object.join()
me = mesh_obj.data

# tira a manga do Meshy
dg = mesh_obj.vertex_groups["_del"].index
bm = bmesh.new(); bm.from_mesh(me); dl = bm.verts.layers.deform.active
bmesh.ops.delete(bm, geom=[v for v in bm.verts if dg in v[dl]], context="VERTS")
bm.to_mesh(me); bm.free()
mesh_obj.vertex_groups.remove(mesh_obj.vertex_groups["_del"])
# onde a boca foi assentada, a luz é a da página (as normais próprias do modelo ainda guardam o relevo)
fg = mesh_obj.vertex_groups["_flat"].index
flat_ids = set(v.index for v in me.vertices if any(g.group == fg for g in v.groups))
if me.has_custom_normals:
    cn = np.empty(len(me.loops) * 3, np.float32); me.corner_normals.foreach_get("vector", cn); cn = cn.reshape(-1, 3)
    lv = np.empty(len(me.loops), np.int32); me.loops.foreach_get("vertex_index", lv)
    sel = np.isin(lv, list(flat_ids)); cn[sel] = (0, -1, 0)
    me.normals_split_custom_set([tuple(v) for v in cn])
mesh_obj.vertex_groups.remove(mesh_obj.vertex_groups["_flat"])
mesh_obj.parent = arm
mod = mesh_obj.modifiers.new("Esqueleto", "ARMATURE"); mod.object = arm

# ------------------------------------------------------------------ animações
pb = arm.pose.bones
REST = {b.name: b.bone.matrix_local.to_3x3() for b in pb}
for b in pb: b.rotation_mode = "QUATERNION"
UP, RIGHT, FRONT = Vector((0, 0, 1)), Vector((1, 0, 0)), Vector((0, -1, 0))

def rot_world(bone, axis, ang):
    """Rotação em torno de um eixo do mundo, convertida para o espaço do osso (relativa ao repouso)."""
    r = REST[bone]
    return (r.inverted() @ Quaternion(axis, ang).to_matrix() @ r).to_quaternion()

def pose_reset():
    for b in pb:
        b.location = (0, 0, 0); b.rotation_quaternion = (1, 0, 0, 0); b.scale = (1, 1, 1)

def apply(pose):
    pose_reset()
    for bone, sp_ in pose.items():
        q = Quaternion()
        for axis_, ang in sp_.get("rot", []):
            q = rot_world(bone, axis_, ang) @ q
        pb[bone].rotation_quaternion = q
        if "loc" in sp_: pb[bone].location = REST[bone].inverted() @ Vector(sp_["loc"])
        if "scale" in sp_: pb[bone].scale = sp_["scale"]

ANIM_BONES = [b.name for b in pb]
def make_clip(name, seconds, fn, step=1):
    act = bpy.data.actions.new(name); act.use_fake_user = True
    arm.animation_data_create(); arm.animation_data.action = act
    frames = int(round(seconds * FPS))
    for f in list(range(0, frames, step)) + [frames]:
        apply(fn(f / FPS, f / frames))
        for bn in ANIM_BONES:
            b = pb[bn]
            b.keyframe_insert("location", frame=f + 1, group=bn)
            b.keyframe_insert("rotation_quaternion", frame=f + 1, group=bn)
            b.keyframe_insert("scale", frame=f + 1, group=bn)
    tr = arm.animation_data.nla_tracks.new(); tr.name = name
    tr.strips.new(name, 1, act); tr.mute = True
    arm.animation_data.action = None
    return act

def S(x): return math.sin(TAU * x)
def C(x): return math.cos(TAU * x)

def merge(*ps):
    out = {}
    for p in ps:
        for k, v in p.items():
            o = out.setdefault(k, {})
            for kk, vv in v.items():
                if kk == "rot": o.setdefault("rot", []).extend(vv)
                elif kk == "loc" and "loc" in o: o["loc"] = tuple(a + b for a, b in zip(o["loc"], vv))
                elif kk == "scale" and "scale" in o: o["scale"] = tuple(a * b for a, b in zip(o["scale"], vv))
                else: o[kk] = vv
    return out

def arm_pose(s, lift=0.0, bend=0.0, hand=0.0, fwd=0.0, fwd_fore=0.0):
    """Braço por articulação: lift = sobe de lado (no plano da frente), fwd = gira para a frente,
    bend = dobra o cotovelo para cima, hand = gira o punho. Ângulos em radianos, iguais nos dois lados."""
    sg = 1 if s == "R" else -1
    return {"arm." + s: {"rot": [(FRONT, sg * lift), (UP, -sg * fwd)]},
            "fore." + s: {"rot": [(FRONT, sg * bend), (UP, -sg * fwd_fore)]},
            "hand." + s: {"rot": [(FRONT, sg * hand)]}}

# Olho fechado = as duas pálpebras se encontram um pouco abaixo do meio do olho (a linha dos cílios
# aparece ali, como no desenho). MEET = latitude do encontro na esfera.
MEET = math.asin(0.45 * EYE["ry"] / RL)
UP_SHUT, LOW_SHUT = -(THETA - MEET) - 0.01, THETA + MEET - 0.01
def lids(up=0.0, low=0.0, upR=None, lowR=None):
    """Pálpebras: 0 = aberta (escondida na página), 1 = fechada (encontram a outra). A de cima desce; a de baixo sobe."""
    out = {}
    for s in "LR":
        u = up if (s == "L" or upR is None) else upR
        d = low if (s == "L" or lowR is None) else lowR
        out["lidU." + s] = {"rot": [(RIGHT, -OPEN + (UP_SHUT + OPEN) * u)]}
        out["lidD." + s] = {"rot": [(RIGHT, OPEN + (LOW_SHUT - OPEN) * d)]}
    return out

def base(u, sway=1.0):
    """Respiração e balanço leve (u = fase 0..1 do ciclo)."""
    br = S(u)
    return merge({
        "body": {"loc": (0, 0, 0.006 * br), "scale": (1 - 0.006 * br, 1 + 0.014 * br, 1 - 0.006 * br), "rot": [(FRONT, 0.02 * sway * S(u + 0.25))]},
        "cap": {"rot": [(FRONT, -0.03 * sway * S(u + 0.1))]},
        "tassel": {"rot": [(FRONT, 0.05 * sway * S(u + 0.05)), (RIGHT, 0.03 * C(u))]},
        "ribbon": {"rot": [(RIGHT, 0.08 * S(u + 0.3)), (FRONT, 0.05 * S(u))]},
    }, arm_pose("L", 0.06 * sway * S(u + 0.1), 0.1 * sway * S(u + 0.2)), arm_pose("R", 0.06 * sway * S(u + 0.35), 0.1 * sway * S(u + 0.45)))

def idle(t, u):
    sh = S(u)
    return merge(base(u), lids(), {"body": {"loc": (0.012 * sh, 0, 0)}, "foot.L": {"loc": (0, 0, 0.02 * max(0, S(u + 0.62)) ** 4)}, "foot.R": {"loc": (0, 0, 0.02 * max(0, S(u + 0.12)) ** 4)}})

def wave(t, u):
    # Tchau: braço para fora e para cima, antebraço em pé, a mão abana com atraso; o corpo acompanha.
    w = S(u * 2)
    return merge(base(u, 0.4), lids(low=0.25), arm_pose("R", 1.3 + 0.06 * w, 1.25 + 0.12 * S(u * 2 - 0.1), 0.5 * S(u * 2 - 0.22), fwd=0.3), {
        "body": {"rot": [(FRONT, -0.06 + 0.015 * w), (UP, 0.1)]},
        "tassel": {"rot": [(FRONT, 0.05 * w)]},
        "brow.L": {"loc": (0, 0, 0.006)}, "brow.R": {"loc": (0, 0, 0.006)},
    })

def happy(t, u):
    # Comemora: pula no lugar, braços para o alto balançando, olhos em meia-lua.
    s = S(u * 2); up = abs(s)
    return merge(base(u, 0.3), lids(low=0.62), {
        "body": {"loc": (0, 0, 0.06 * up), "scale": (1 + 0.015 * (1 - up), 1 - 0.03 * (1 - up), 1), "rot": [(FRONT, 0.08 * S(u)), (UP, 0.15 * S(u))]},
        "foot.L": {"loc": (0, 0, 0.09 * max(0, s)), "rot": [(RIGHT, -0.4 * max(0, s))]},
        "foot.R": {"loc": (0, 0, 0.09 * max(0, -s)), "rot": [(RIGHT, -0.4 * max(0, -s))]},
        "tassel": {"rot": [(FRONT, 0.1 * S(u * 2 + 0.15)), (RIGHT, 0.06 * up)]},
        "cap": {"rot": [(FRONT, -0.06 * S(u))]},
        "brow.L": {"loc": (0, 0, 0.012)}, "brow.R": {"loc": (0, 0, 0.012)},
    }, arm_pose("R", 1.55 + 0.15 * S(u * 2 + 0.1), 0.6, 0.35 * S(u * 2)), arm_pose("L", 1.55 + 0.15 * S(u * 2 + 0.35), 0.6, 0.35 * S(u * 2 + 0.25)))

def talking(t, u):
    g = S(u * 2)
    return merge(base(u, 0.6), lids(), {
        "body": {"loc": (0, 0, 0.008 * abs(S(u * 4))), "rot": [(UP, 0.08 * S(u)), (FRONT, 0.02 * g)]},
        "brow.L": {"loc": (0, 0, 0.004 * max(0, S(u * 3)))}, "brow.R": {"loc": (0, 0, 0.004 * max(0, S(u * 3 + 0.2)))},
    }, arm_pose("R", 0.45 + 0.12 * g, 1.15 + 0.25 * S(u * 2 + 0.15), 0.3 * S(u * 2 + 0.3), fwd=0.75), arm_pose("L", 0.12, 0.4 + 0.1 * S(u + 0.3), fwd=0.3))

def thinking(t, u):
    # Pensando: a mão vem para perto dos óculos, olha para cima, uma sobrancelha erguida.
    tap = 0.06 * max(0.0, S(u * 2)) ** 2
    return merge(base(u, 0.4), lids(up=0.12), arm_pose("R", 0.6, 2.05 + tap, 0.5, fwd=1.0), arm_pose("L", 0.05, 0.6, fwd=0.55), {
        "body": {"rot": [(FRONT, 0.06), (RIGHT, 0.04), (UP, 0.06)]},
        "eye.L": {"rot": [(RIGHT, -0.12), (UP, 0.12)]},
        "eye.R": {"rot": [(RIGHT, -0.12), (UP, 0.12)]},
        "brow.R": {"loc": (0, 0, 0.014), "rot": [(FRONT, 0.1)]},
        "brow.L": {"loc": (0, 0, -0.002), "rot": [(FRONT, 0.05)]},
    })

def point(t, u):
    return merge(base(u, 0.4), lids(), arm_pose("R", 0.95 + 0.02 * S(u), 0.12, 0.2, fwd=0.35), {
        "body": {"rot": [(UP, -0.18), (FRONT, -0.04)]},
        "eye.L": {"rot": [(UP, -0.15)]}, "eye.R": {"rot": [(UP, -0.15)]},
        "brow.L": {"loc": (0, 0, 0.006)}, "brow.R": {"loc": (0, 0, 0.006)},
    })

def sad(t, u):
    # Errou: "não" com a cabeça no começo, depois murcha: pálpebras pela metade, olhar para baixo,
    # sobrancelhas com a ponta de dentro para cima, braços caídos para a frente (o app toca uma vez).
    k = min(1.0, t / 0.6); e = k * k * (3 - 2 * k)
    shake = math.sin(t * 16) * 0.16 * max(0.0, 1 - t / 1.3) if t < 1.3 else 0
    return merge(base(u, 0.2), lids(up=0.5 * e), {
        "body": {"loc": (0, 0, -0.035 * e), "scale": (1, 1 - 0.02 * e, 1), "rot": [(RIGHT, 0.14 * e), (UP, shake)]},
        "brow.L": {"rot": [(FRONT, 0.32 * e)], "loc": (0, 0, 0.004 * e)},
        "brow.R": {"rot": [(FRONT, -0.32 * e)], "loc": (0, 0, 0.004 * e)},
        "eye.L": {"rot": [(RIGHT, 0.14 * e)]}, "eye.R": {"rot": [(RIGHT, 0.14 * e)]},
        "cap": {"rot": [(FRONT, 0.06 * e), (RIGHT, 0.05 * e)]},
        "tassel": {"rot": [(RIGHT, 0.08 * e)]},
    }, arm_pose("R", -0.2 * e, 0.35 * e, -0.25 * e, fwd=0.4 * e), arm_pose("L", -0.2 * e, 0.35 * e, -0.25 * e, fwd=0.4 * e))

def run(t, u):
    s, c = S(u), C(u)
    return merge(lids(), {
        "body": {"loc": (0, 0, 0.035 * abs(s)), "rot": [(RIGHT, 0.2), (UP, 0.06 * s)]},
        "foot.L": {"loc": (0, -0.13 * s, 0.09 * max(0, c)), "rot": [(RIGHT, -0.6 * max(0, c) + 0.25 * s)]},
        "foot.R": {"loc": (0, 0.13 * s, 0.09 * max(0, -c)), "rot": [(RIGHT, -0.6 * max(0, -c) - 0.25 * s)]},
        "tassel": {"rot": [(RIGHT, -0.3 + 0.08 * abs(s)), (FRONT, 0.06 * s)]},
        "ribbon": {"rot": [(RIGHT, -0.4 + 0.1 * s)]},
        "cap": {"rot": [(RIGHT, -0.05)]},
    }, arm_pose("L", 0.2, 1.2, fwd=0.8 * s), arm_pose("R", 0.2, 1.2, fwd=-0.8 * s))

def jump(t, u):
    # Pulo do toque: agacha, sobe, cai e amortece.
    if u < 0.2: k = math.sin(u / 0.2 * math.pi / 2); y, sy = -0.03 * k, 1 - 0.08 * k
    elif u < 0.65: q = (u - 0.2) / 0.45; y, sy = 0.28 * math.sin(q * math.pi), 1 + 0.06 * math.sin(q * math.pi)
    else: q = (u - 0.65) / 0.35; y, sy = -0.025 * math.sin(q * math.pi), 1 - 0.07 * math.sin(q * math.pi) * (1 - q)
    a = max(0.0, y) / 0.28
    return merge(lids(low=0.3 * a), {
        "root": {"loc": (0, 0, max(0.0, y))},
        "body": {"loc": (0, 0, min(0.0, y)), "scale": (2 - sy, sy, 2 - sy)},
        "foot.L": {"rot": [(RIGHT, -0.35 * a)]}, "foot.R": {"rot": [(RIGHT, -0.35 * a)]},
        "tassel": {"rot": [(RIGHT, 0.25 * a)]},
        "glasses": {"loc": (0, 0, -0.015 * a)},
        "brow.L": {"loc": (0, 0, 0.01 * a)}, "brow.R": {"loc": (0, 0, 0.01 * a)},
    }, arm_pose("R", 1.3 * a, 0.6 * a), arm_pose("L", 1.3 * a, 0.6 * a))

def eyes_shut(t, u):
    # Referência para o app: as pálpebras fechadas (o piscar e a piscadela vão até aqui).
    return lids(1.0, 1.0)

CLIPS = [("eyes_shut", 0.1, eyes_shut), ("idle", 4.0, idle), ("wave", 1.6, wave), ("happy", 1.2, happy), ("talking", 3.0, talking),
         ("thinking", 4.0, thinking), ("point", 3.0, point), ("sad", 2.4, sad), ("run", 0.5, run), ("jump", 0.8, jump)]
# Piscar e piscadela o app faz levando as pálpebras até a pose de "eyes_shut"; a boca falando, escalando o
# osso "mouth"; o quadro da boca (sorriso, sorrisão, "hum", triste), deslocando a textura do material "boca".
for name, secs, fn in CLIPS:
    make_clip(name, secs, fn)
pose_reset()
scene.frame_start, scene.frame_end = 1, 1

# ------------------------------------------------------------------ texturas menores e exportação
for img, size in ((base_img, 2048), (image_into(bsdf.inputs["Normal"]), 1024), (image_into(bsdf.inputs["Roughness"]), 512)):
    if img and img.size[0] > size: img.scale(size, size)

if BLEND: bpy.ops.wm.save_as_mainfile(filepath=BLEND)
props = bpy.ops.export_scene.gltf.get_rna_type().properties.keys()
opts = dict(filepath=OUT, export_format="GLB", export_animations=True, export_skins=True, export_yup=True, use_selection=False)
for k, v in (("export_animation_mode", "ACTIONS"), ("export_image_format", "JPEG"), ("export_jpeg_quality", 86), ("export_image_quality", 86),
             ("export_force_sampling", True), ("export_def_bones", False), ("export_optimize_animation_size", True), ("export_anim_slide_to_zero", True),
             ("export_bake_animation", False), ("export_morph", False), ("export_lights", False), ("export_cameras", False)):
    if k in props: opts[k] = v
for o in scene.objects: o.select_set(True)
bpy.ops.export_scene.gltf(**opts)
print("ok", OUT)
