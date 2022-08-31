import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEcampagne } from '../ecampagne.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ecampagne.test-samples';

import { EcampagneService, RestEcampagne } from './ecampagne.service';

const requireRestSample: RestEcampagne = {
  ...sampleWithRequiredData,
  debutcampagne: sampleWithRequiredData.debutcampagne?.toJSON(),
  fincampagne: sampleWithRequiredData.fincampagne?.toJSON(),
  debutreelcamp: sampleWithRequiredData.debutreelcamp?.toJSON(),
  finreelcamp: sampleWithRequiredData.finreelcamp?.toJSON(),
};

describe('Ecampagne Service', () => {
  let service: EcampagneService;
  let httpMock: HttpTestingController;
  let expectedResult: IEcampagne | IEcampagne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EcampagneService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Ecampagne', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ecampagne = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ecampagne).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ecampagne', () => {
      const ecampagne = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ecampagne).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ecampagne', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ecampagne', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ecampagne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEcampagneToCollectionIfMissing', () => {
      it('should add a Ecampagne to an empty array', () => {
        const ecampagne: IEcampagne = sampleWithRequiredData;
        expectedResult = service.addEcampagneToCollectionIfMissing([], ecampagne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ecampagne);
      });

      it('should not add a Ecampagne to an array that contains it', () => {
        const ecampagne: IEcampagne = sampleWithRequiredData;
        const ecampagneCollection: IEcampagne[] = [
          {
            ...ecampagne,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEcampagneToCollectionIfMissing(ecampagneCollection, ecampagne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ecampagne to an array that doesn't contain it", () => {
        const ecampagne: IEcampagne = sampleWithRequiredData;
        const ecampagneCollection: IEcampagne[] = [sampleWithPartialData];
        expectedResult = service.addEcampagneToCollectionIfMissing(ecampagneCollection, ecampagne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ecampagne);
      });

      it('should add only unique Ecampagne to an array', () => {
        const ecampagneArray: IEcampagne[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ecampagneCollection: IEcampagne[] = [sampleWithRequiredData];
        expectedResult = service.addEcampagneToCollectionIfMissing(ecampagneCollection, ...ecampagneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ecampagne: IEcampagne = sampleWithRequiredData;
        const ecampagne2: IEcampagne = sampleWithPartialData;
        expectedResult = service.addEcampagneToCollectionIfMissing([], ecampagne, ecampagne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ecampagne);
        expect(expectedResult).toContain(ecampagne2);
      });

      it('should accept null and undefined values', () => {
        const ecampagne: IEcampagne = sampleWithRequiredData;
        expectedResult = service.addEcampagneToCollectionIfMissing([], null, ecampagne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ecampagne);
      });

      it('should return initial array if no Ecampagne is added', () => {
        const ecampagneCollection: IEcampagne[] = [sampleWithRequiredData];
        expectedResult = service.addEcampagneToCollectionIfMissing(ecampagneCollection, undefined, null);
        expect(expectedResult).toEqual(ecampagneCollection);
      });
    });

    describe('compareEcampagne', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEcampagne(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEcampagne(entity1, entity2);
        const compareResult2 = service.compareEcampagne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEcampagne(entity1, entity2);
        const compareResult2 = service.compareEcampagne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEcampagne(entity1, entity2);
        const compareResult2 = service.compareEcampagne(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
