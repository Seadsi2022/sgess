import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EquestionnaireFormService, EquestionnaireFormGroup } from './equestionnaire-form.service';
import { IEquestionnaire } from '../equestionnaire.model';
import { EquestionnaireService } from '../service/equestionnaire.service';
import { IEcampagne } from 'app/entities/ecampagne/ecampagne.model';
import { EcampagneService } from 'app/entities/ecampagne/service/ecampagne.service';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';

@Component({
  selector: 'jhi-equestionnaire-update',
  templateUrl: './equestionnaire-update.component.html',
})
export class EquestionnaireUpdateComponent implements OnInit {
  isSaving = false;
  equestionnaire: IEquestionnaire | null = null;

  equestionnairesSharedCollection: IEquestionnaire[] = [];
  ecampagnesSharedCollection: IEcampagne[] = [];
  scodevaleursSharedCollection: IScodevaleur[] = [];

  editForm: EquestionnaireFormGroup = this.equestionnaireFormService.createEquestionnaireFormGroup();

  constructor(
    protected equestionnaireService: EquestionnaireService,
    protected equestionnaireFormService: EquestionnaireFormService,
    protected ecampagneService: EcampagneService,
    protected scodevaleurService: ScodevaleurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEquestionnaire = (o1: IEquestionnaire | null, o2: IEquestionnaire | null): boolean =>
    this.equestionnaireService.compareEquestionnaire(o1, o2);

  compareEcampagne = (o1: IEcampagne | null, o2: IEcampagne | null): boolean => this.ecampagneService.compareEcampagne(o1, o2);

  compareScodevaleur = (o1: IScodevaleur | null, o2: IScodevaleur | null): boolean => this.scodevaleurService.compareScodevaleur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equestionnaire }) => {
      this.equestionnaire = equestionnaire;
      if (equestionnaire) {
        this.updateForm(equestionnaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equestionnaire = this.equestionnaireFormService.getEquestionnaire(this.editForm);
    if (equestionnaire.id !== null) {
      this.subscribeToSaveResponse(this.equestionnaireService.update(equestionnaire));
    } else {
      this.subscribeToSaveResponse(this.equestionnaireService.create(equestionnaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquestionnaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(equestionnaire: IEquestionnaire): void {
    this.equestionnaire = equestionnaire;
    this.equestionnaireFormService.resetForm(this.editForm, equestionnaire);

    this.equestionnairesSharedCollection = this.equestionnaireService.addEquestionnaireToCollectionIfMissing<IEquestionnaire>(
      this.equestionnairesSharedCollection,
      equestionnaire.parent
    );
    this.ecampagnesSharedCollection = this.ecampagneService.addEcampagneToCollectionIfMissing<IEcampagne>(
      this.ecampagnesSharedCollection,
      equestionnaire.ecampagne
    );
    this.scodevaleursSharedCollection = this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(
      this.scodevaleursSharedCollection,
      equestionnaire.typestructure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equestionnaireService
      .query()
      .pipe(map((res: HttpResponse<IEquestionnaire[]>) => res.body ?? []))
      .pipe(
        map((equestionnaires: IEquestionnaire[]) =>
          this.equestionnaireService.addEquestionnaireToCollectionIfMissing<IEquestionnaire>(equestionnaires, this.equestionnaire?.parent)
        )
      )
      .subscribe((equestionnaires: IEquestionnaire[]) => (this.equestionnairesSharedCollection = equestionnaires));

    this.ecampagneService
      .query()
      .pipe(map((res: HttpResponse<IEcampagne[]>) => res.body ?? []))
      .pipe(
        map((ecampagnes: IEcampagne[]) =>
          this.ecampagneService.addEcampagneToCollectionIfMissing<IEcampagne>(ecampagnes, this.equestionnaire?.ecampagne)
        )
      )
      .subscribe((ecampagnes: IEcampagne[]) => (this.ecampagnesSharedCollection = ecampagnes));

    this.scodevaleurService
      .query()
      .pipe(map((res: HttpResponse<IScodevaleur[]>) => res.body ?? []))
      .pipe(
        map((scodevaleurs: IScodevaleur[]) =>
          this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(scodevaleurs, this.equestionnaire?.typestructure)
        )
      )
      .subscribe((scodevaleurs: IScodevaleur[]) => (this.scodevaleursSharedCollection = scodevaleurs));
  }
}
